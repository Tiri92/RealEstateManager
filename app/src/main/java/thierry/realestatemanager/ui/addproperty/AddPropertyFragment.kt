package thierry.realestatemanager.ui.addproperty

import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import thierry.realestatemanager.R
import thierry.realestatemanager.databinding.FragmentAddUpdatePropertyBinding
import thierry.realestatemanager.model.Address
import thierry.realestatemanager.model.Media
import thierry.realestatemanager.model.PointsOfInterest
import thierry.realestatemanager.model.Property
import thierry.realestatemanager.utils.MediaUtils
import thierry.realestatemanager.utils.RegexUtils
import thierry.realestatemanager.utils.Utils
import java.lang.reflect.Field
import java.util.*

@AndroidEntryPoint
class AddPropertyFragment : AddPropertyAdapter.PhotoDescriptionChanged, Fragment() {

    private val viewModel: AddPropertyViewModel by viewModels()
    private var _binding: FragmentAddUpdatePropertyBinding? = null
    private val binding get() = _binding!!
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var activityResultLauncherForVideo: ActivityResultLauncher<Intent>
    lateinit var resultPropertyTypeSpinner: String
    lateinit var resultPropertyCountrySpinner: String
    lateinit var resultPropertyAgentSpinner: String
    private var lastIndexValue: Int? = null
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddUpdatePropertyBinding.inflate(inflater, container, false)
        val rootView = binding.root

        val recyclerView: RecyclerView = binding.recyclerviewFragmentAddAndUpdate
        binding.isSoldSwitch.isVisible = false
        setHasOptionsMenu(true)

        binding.price.helperText = getString(R.string.field_empty)
        binding.rooms.helperText = getString(R.string.field_empty)
        binding.bedrooms.helperText = getString(R.string.field_empty)
        binding.bathrooms.helperText = getString(R.string.field_empty)
        binding.surface.helperText = getString(R.string.field_empty)
        binding.description.helperText = getString(R.string.field_empty)
        binding.city.helperText = getString(R.string.field_empty)
        binding.postcode.helperText = getString(R.string.field_empty)
        binding.street.helperText = getString(R.string.field_empty)

        val propertyAgentSpinner: AppCompatSpinner = binding.propertyAgentSpinner
        val propertyAgentAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.PropertiesAgents,
            android.R.layout.simple_spinner_item
        )
        propertyAgentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        propertyAgentSpinner.adapter = propertyAgentAdapter
        propertyAgentSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long,
                ) {
                    val aSpinnerResult: String =
                        parent?.getItemAtPosition(position).toString()
                    resultPropertyAgentSpinner = aSpinnerResult
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }

        val propertyTypeSpinner: AppCompatSpinner = binding.typeOfPropertySpinner
        val propertyTypeAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.PropertiesTypes,
            android.R.layout.simple_spinner_item
        )
        propertyTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        propertyTypeSpinner.adapter = propertyTypeAdapter
        propertyTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                p1: View?,
                position: Int,
                p3: Long,
            ) {
                val tSpinnerResult: String = parent?.getItemAtPosition(position).toString()
                resultPropertyTypeSpinner = tSpinnerResult
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        val propertyCountrySpinner: AppCompatSpinner = binding.countrySpinner
        val propertyCountryAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.Countries,
            android.R.layout.simple_spinner_item
        )
        propertyCountryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        propertyCountrySpinner.adapter = propertyCountryAdapter
        propertyCountrySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long,
                ) {
                    val cSpinnerResult: String = parent?.getItemAtPosition(position).toString()
                    resultPropertyCountrySpinner = cSpinnerResult
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }

        viewModel.getLastIdPropertyTable.observe(viewLifecycleOwner) { lastIndexValue ->
            this.lastIndexValue = lastIndexValue
        }

        val saveButton: AppCompatButton = binding.saveButton
        saveButton.setOnClickListener {

            if (!viewModel.getListOfMedia().value.isNullOrEmpty()) {

                var schoolState = false
                var universityState = false
                var parksState = false
                var sportsClubsState = false
                var stationsState = false
                var shoppingCentreState = false
                val pointsOfInterestChipGroup: ChipGroup = binding.pointsOfInterestChipGroup
                pointsOfInterestChipGroup.checkedChipIds.forEach { chipItem ->
                    val chipText =
                        binding.pointsOfInterestChipGroup.findViewById<Chip>(chipItem).text.toString()
                    val chipState =
                        binding.pointsOfInterestChipGroup.findViewById<Chip>(chipItem).isChecked
                    if (Locale.getDefault().displayLanguage.equals("français")) {
                        when (chipText) {
                            "École" -> schoolState = chipState
                            "Université" -> universityState = chipState
                            "Parcs" -> parksState = chipState
                            "Clubs sportifs" -> sportsClubsState = chipState
                            "Stations" -> stationsState = chipState
                            "Centre commercial" -> shoppingCentreState = chipState
                        }
                    } else {
                        when (chipText) {
                            "School" -> schoolState = chipState
                            "University" -> universityState = chipState
                            "Parks" -> parksState = chipState
                            "Sports clubs" -> sportsClubsState = chipState
                            "Stations" -> stationsState = chipState
                            "Shopping centre" -> shoppingCentreState = chipState
                        }
                    }
                }
                viewModel.insertPointsOfInterest(
                    PointsOfInterest(
                        propertyId = lastIndexValue!!,
                        school = schoolState,
                        university = universityState,
                        parks = parksState,
                        sportsClubs = sportsClubsState,
                        stations = stationsState,
                        shoppingCenter = shoppingCentreState
                    )
                )

                viewModel.insertProperty(
                    Property(
                        price = binding.priceEditText.text.toString().toInt(),
                        type = resultPropertyTypeSpinner,
                        address = Address(
                            city = resultPropertyCountrySpinner,
                            street = binding.streetEditText.text.toString(),
                            postcode = binding.postcodeEditText.text.toString().toInt(),
                            country = resultPropertyCountrySpinner
                        ),
                        numberOfRooms = binding.roomsEditText.text.toString().toInt(),
                        numberOfBedrooms = binding.bedroomsEditText.text.toString().toInt(),
                        numberOfBathrooms = binding.bathroomsEditText.text.toString().toInt(),
                        surface = binding.surfaceEditText.text.toString().toInt(),
                        description = binding.descriptionEditText.text.toString(),
                        propertyAgent = resultPropertyAgentSpinner,
                        dateOfCreation = Utils.getTodayDate()
                    )
                )

                if (viewModel.getListOfMedia().value != null) {
                    viewModel.getListOfMedia().value!!.forEachIndexed { index, media ->
                        viewModel.insertMedia(
                            Media(
                                uri = media.uri,
                                propertyId = lastIndexValue!!,
                                description = media.description,
                                position = index
                            )
                        )
                    }
                }

                viewModel.propertySuccessfullyInserted()
                    .observe(viewLifecycleOwner) { isSuccessfullyInserted ->
                        val title = getString(R.string.property_successfully_added)
                        val message = getString(R.string.good_luck)
                        displayNotification(title, message)
                        if (isSuccessfullyInserted.toInt() == lastIndexValue) {
                            val navHostFragment =
                                requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment_property_detail) as NavHostFragment
                            navController = navHostFragment.navController
                            navController.navigateUp()
                        }
                    }

            } else {
                Snackbar.make(requireView(), getString(R.string.add_at_least_one_media), Snackbar.LENGTH_LONG).show()
            }

        }


        fun updateSaveButtonState() {
            RegexUtils.updateButtonState(saveButton,
                binding.priceEditText.text,
                binding.roomsEditText.text,
                binding.bedroomsEditText.text,
                binding.bathroomsEditText.text,
                binding.surfaceEditText.text,
                binding.descriptionEditText.text,
                binding.cityEditText.text,
                binding.postcodeEditText.text,
                binding.streetEditText.text)
        }
        updateSaveButtonState()

        //PHOTO FROM GALLERY
        val getImageFromGalleryLauncher = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->
            val fileDate: String = Utils.getTodayFormattedDateForMediaUri()
            val fileName = "Photo"
            val uriPhoto: String = context?.filesDir.toString() + "/" + "$fileName$fileDate.jpg"
            if (uri != null) {
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(
                    context?.applicationContext?.contentResolver,
                    uri
                )
                if (MediaUtils.savePhotoToInternalMemory(
                        fileName,
                        fileDate,
                        bitmap,
                        requireContext()
                    )
                ) {
                    viewModel.addMedia(Media(propertyId = lastIndexValue!!,
                        uri = uriPhoto,
                        description = ""))
                }
            }
        }

        //PHOTO FROM CAMERA
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult? ->
                if (result!!.resultCode == Activity.RESULT_OK) {
                    val fileDate: String = Utils.getTodayFormattedDateForMediaUri()
                    val fileName = "Photo"
                    val uriPhoto: String =
                        context?.filesDir.toString() + "/" + "$fileName$fileDate.jpg"
                    val bitmap = result.data!!.extras!!.get("data") as Bitmap
                    if (MediaUtils.savePhotoToInternalMemory(
                            fileName,
                            fileDate,
                            bitmap,
                            requireContext()
                        )
                    ) {
                        viewModel.addMedia(Media(propertyId = lastIndexValue!!,
                            uri = uriPhoto,
                            description = ""))
                    }
                }
            }

        //VIDEO FROM CAMERA
        activityResultLauncherForVideo =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult? ->
                if (result!!.resultCode == Activity.RESULT_OK) {
                    val fileName = "Video"
                    val fileDate: String = Utils.getTodayFormattedDateForMediaUri()
                    val uriVideo =
                        context?.filesDir.toString() + "/" + "$fileName$fileDate.mp4"
                    if (MediaUtils.saveVideoToInternalMemory(fileName,
                            fileDate,
                            result.data?.data.toString().toUri(),
                            requireContext())
                    ) {
                        viewModel.addMedia(
                            Media(
                                uri = uriVideo,
                                description = "",
                                propertyId = lastIndexValue!!
                            )
                        )
                    }
                }
            }

        //VIDEO FROM GALlERY
        val getVideoFromGalleryLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()
            ) { uri ->
                if (uri != null) {
                    val fileName = "Video"
                    val fileDate: String = Utils.getTodayFormattedDateForMediaUri()
                    val uriVideo =
                        context?.filesDir.toString() + "/" + "$fileName$fileDate.mp4"
                    if (
                        MediaUtils.saveVideoToInternalMemory(fileName,
                            fileDate,
                            uri,
                            requireContext())
                    ) {
                        viewModel.addMedia(
                            Media(
                                uri = uriVideo,
                                description = "",
                                propertyId = lastIndexValue!!
                            )
                        )
                    }
                }
            }

        binding.mediaButton.setOnClickListener {
            popupMenu(binding.mediaButton,
                getImageFromGalleryLauncher,
                activityResultLauncher,
                activityResultLauncherForVideo,
                getVideoFromGalleryLauncher)
        }

        viewModel.getListOfMedia().observe(viewLifecycleOwner, {

            val simpleCallback = object :
                ItemTouchHelper.SimpleCallback(ItemTouchHelper.START or ItemTouchHelper.END,
                    0) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder,
                ): Boolean {

                    val fromPosition = viewHolder.adapterPosition
                    val toPosition = target.adapterPosition

                    Collections.swap(it, fromPosition, toPosition)

                    recyclerView.adapter?.notifyItemMoved(fromPosition, toPosition)

                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                }

                override fun onSelectedChanged(
                    viewHolder: RecyclerView.ViewHolder?,
                    actionState: Int,
                ) {
                    super.onSelectedChanged(viewHolder, actionState)
                    if (actionState == 2) {
                        viewHolder?.itemView?.setBackgroundColor(ContextCompat.getColor(
                            requireContext(),
                            R.color.backgroundItem))
                    }
                }

                override fun clearView(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                ) {
                    super.clearView(recyclerView, viewHolder)
                    viewHolder.itemView.setBackgroundColor(0)
                }

            }
            val itemTouchHelper = ItemTouchHelper(simpleCallback)
            itemTouchHelper.attachToRecyclerView(recyclerView)

            setUpRecyclerView(recyclerView, it, childFragmentManager)
        })

        binding.priceEditText.addTextChangedListener {
            binding.price.helperText = RegexUtils.emptyTextVerification(binding.priceEditText.text)
            updateSaveButtonState()
        }
        binding.priceEditText.setOnFocusChangeListener { _, _ ->
            binding.price.helperText = RegexUtils.emptyTextVerification(binding.priceEditText.text)
        }

        binding.roomsEditText.addTextChangedListener {
            binding.rooms.helperText = RegexUtils.emptyTextVerification(binding.roomsEditText.text)
            updateSaveButtonState()
        }
        binding.roomsEditText.setOnFocusChangeListener { _, _ ->
            binding.rooms.helperText = RegexUtils.emptyTextVerification(binding.roomsEditText.text)
        }

        binding.bedroomsEditText.addTextChangedListener {
            binding.bedrooms.helperText =
                RegexUtils.emptyTextVerification(binding.bedroomsEditText.text)
            updateSaveButtonState()
        }
        binding.bedroomsEditText.setOnFocusChangeListener { _, _ ->
            binding.bedrooms.helperText =
                RegexUtils.emptyTextVerification(binding.bedroomsEditText.text)
        }

        binding.bathroomsEditText.addTextChangedListener {
            binding.bathrooms.helperText =
                RegexUtils.emptyTextVerification(binding.bathroomsEditText.text)
            updateSaveButtonState()
        }
        binding.bathroomsEditText.setOnFocusChangeListener { _, _ ->
            binding.bathrooms.helperText =
                RegexUtils.emptyTextVerification(binding.bathroomsEditText.text)
        }

        binding.surfaceEditText.addTextChangedListener {
            binding.surface.helperText =
                RegexUtils.emptyTextVerification(binding.surfaceEditText.text)
            updateSaveButtonState()
        }
        binding.surfaceEditText.setOnFocusChangeListener { _, _ ->
            binding.surface.helperText =
                RegexUtils.emptyTextVerification(binding.surfaceEditText.text)
        }

        binding.descriptionEditText.addTextChangedListener {
            binding.description.helperText =
                RegexUtils.emptyTextVerification(binding.descriptionEditText.text)
            updateSaveButtonState()
        }
        binding.descriptionEditText.setOnFocusChangeListener { _, _ ->
            binding.description.helperText =
                RegexUtils.emptyTextVerification(binding.descriptionEditText.text)
        }

        binding.cityEditText.addTextChangedListener {
            binding.city.helperText = RegexUtils.emptyTextVerification(binding.cityEditText.text)
            updateSaveButtonState()
        }
        binding.cityEditText.setOnFocusChangeListener { _, _ ->
            binding.city.helperText = RegexUtils.emptyTextVerification(binding.cityEditText.text)
        }

        binding.postcodeEditText.addTextChangedListener {
            binding.postcode.helperText =
                RegexUtils.emptyTextVerification(binding.postcodeEditText.text)
            updateSaveButtonState()
        }
        binding.postcodeEditText.setOnFocusChangeListener { _, _ ->
            binding.postcode.helperText =
                RegexUtils.emptyTextVerification(binding.postcodeEditText.text)
        }

        binding.streetEditText.addTextChangedListener {
            binding.street.helperText =
                RegexUtils.emptyTextVerification(binding.streetEditText.text)
            updateSaveButtonState()
        }
        binding.streetEditText.setOnFocusChangeListener { _, _ ->
            binding.street.helperText =
                RegexUtils.emptyTextVerification(binding.streetEditText.text)
        }

        return rootView
    }

    private fun displayNotification(task: String, desc: String) {
        val manager =
            requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel("RealEstateManager",
                    "RealEstateManager",
                    NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }
        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(requireContext(), "RealEstateManager")
                .setContentTitle(task)
                .setStyle(NotificationCompat.BigTextStyle().bigText(desc))
                .setDefaults(Notification.DEFAULT_SOUND)
                .setSmallIcon(R.mipmap.ic_logo)
        manager.notify(1, builder.build())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.edit).isVisible = false
        menu.findItem(R.id.add).isVisible = false
        menu.findItem(R.id.map).isVisible = false
        menu.findItem(R.id.filter).isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setUpRecyclerView(
        recyclerView: RecyclerView,
        listOfPropertyMedia: List<Media>,
        supportFragmentManager: FragmentManager,
    ) {
        val myLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = myLayoutManager
        recyclerView.adapter = AddPropertyAdapter(listOfPropertyMedia, this, supportFragmentManager)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDescriptionPhotoChanged(description: String, uri: String) {
        viewModel.setDescriptionOfMedia(description, uri)
    }

    override fun onDeleteMedia(media: Media) {
        context?.deleteFile(media.uri.substringAfterLast("/"))
        viewModel.deleteMedia(media)
    }

    private fun popupMenu(
        view: View,
        getImageFromGalleryLauncher: ActivityResultLauncher<String>,
        activityResultLauncher: ActivityResultLauncher<Intent>,
        activityResultLauncherForVideo: ActivityResultLauncher<Intent>,
        getVideoFromGalleryLauncher: ActivityResultLauncher<String>,
    ) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.inflate(R.menu.popup_menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.photo_from_gallery -> {
                    getImageFromGalleryLauncher.launch("image/*")
                    true
                }
                R.id.photo_from_camera -> {
                    activityResultLauncher.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
                    true
                }
                R.id.video_from_gallery -> {
                    getVideoFromGalleryLauncher.launch("video/*")
                    true
                }
                R.id.video_from_camera -> {
                    activityResultLauncherForVideo.launch(Intent(MediaStore.ACTION_VIDEO_CAPTURE))
                    true
                }
                else -> {
                    true
                }
            }
        }
        val popup: Field = PopupMenu::class.java.getDeclaredField("mPopup")
        popup.isAccessible = true
        val menu: Any? = popup.get(popupMenu)
        menu?.javaClass?.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
            ?.invoke(menu, true)
        popupMenu.show()
    }

}