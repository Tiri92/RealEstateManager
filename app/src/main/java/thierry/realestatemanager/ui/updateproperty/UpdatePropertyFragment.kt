package thierry.realestatemanager.ui.updateproperty

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.switchmaterial.SwitchMaterial
import dagger.hilt.android.AndroidEntryPoint
import thierry.realestatemanager.R
import thierry.realestatemanager.databinding.FragmentAddUpdatePropertyBinding
import thierry.realestatemanager.model.*
import thierry.realestatemanager.utils.MediaUtils
import thierry.realestatemanager.utils.RegexUtils
import thierry.realestatemanager.utils.Utils
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class UpdatePropertyFragment : UpdatePropertyAdapter.PhotoDescriptionChanged, Fragment() {

    private var _binding: FragmentAddUpdatePropertyBinding? = null
    private val viewModel by viewModels<UpdatePropertyViewModel>()
    private val binding get() = _binding!!
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var activityResultLauncherForVideo: ActivityResultLauncher<Intent>
    lateinit var resultPropertyTypeSpinner: String
    lateinit var resultPropertyCountrySpinner: String
    private lateinit var currentFullProperty: FullProperty
    private lateinit var navController: NavController
    private var dateOfSale: Long? = null
    private var isSold: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddUpdatePropertyBinding.inflate(inflater, container, false)
        val rootView = binding.root
        val recyclerView: RecyclerView = binding.recyclerviewFragmentAddAndUpdate

        binding.title.text = getString(R.string.update_property)
        binding.saveButton.text = getString(R.string.save_change)
        binding.price.isHelperTextEnabled = false
        binding.rooms.isHelperTextEnabled = false
        binding.bedrooms.isHelperTextEnabled = false
        binding.bathrooms.isHelperTextEnabled = false
        binding.surface.isHelperTextEnabled = false
        binding.description.isHelperTextEnabled = false
        binding.city.isHelperTextEnabled = false
        binding.postcode.isHelperTextEnabled = false
        binding.street.isHelperTextEnabled = false

        val propertyTypeSpinner: AppCompatSpinner = binding.typeOfPropertySpinner
        val propertyTypeAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.PropertiesTypes,
            android.R.layout.simple_spinner_item
        )
        propertyTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        propertyTypeSpinner.adapter = propertyTypeAdapter
        propertyTypeSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long,
                ) {
                    val tSpinnerResult: String =
                        parent?.getItemAtPosition(position).toString()
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
                    val cSpinnerResult: String =
                        parent?.getItemAtPosition(position).toString()
                    resultPropertyCountrySpinner = cSpinnerResult
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }

        viewModel.getCurrentFullProperty().observe(viewLifecycleOwner) { currentFullProperty ->
            this.currentFullProperty = currentFullProperty
            if (currentFullProperty != null) {
                binding.priceEditText.setText(currentFullProperty.property.price.toString())
                binding.roomsEditText.setText(currentFullProperty.property.numberOfRooms.toString())
                binding.bedroomsEditText.setText(currentFullProperty.property.numberOfBedrooms.toString())
                binding.bathroomsEditText.setText(currentFullProperty.property.numberOfBathrooms.toString())
                binding.surfaceEditText.setText(currentFullProperty.property.surface.toString())
                binding.descriptionEditText.setText(currentFullProperty.property.description)
                binding.cityEditText.setText(currentFullProperty.property.address!!.city)
                binding.postcodeEditText.setText(currentFullProperty.property.address.postcode.toString())
                binding.streetEditText.setText(currentFullProperty.property.address.street)

                when (currentFullProperty.pointsOfInterestList[0].school) {
                    true -> binding.schoolChip.isChecked = true
                    false -> binding.schoolChip.isChecked = false
                }

                when (currentFullProperty.pointsOfInterestList[0].parks) {
                    true -> binding.parksChip.isChecked = true
                    false -> binding.parksChip.isChecked = false
                }

                when (currentFullProperty.pointsOfInterestList[0].shoppingCenter) {
                    true -> binding.shoppingCentreChip.isChecked = true
                    false -> binding.shoppingCentreChip.isChecked = false
                }

                when (currentFullProperty.pointsOfInterestList[0].sportsClubs) {
                    true -> binding.sportsClubsChip.isChecked = true
                    false -> binding.sportsClubsChip.isChecked = false
                }

                when (currentFullProperty.pointsOfInterestList[0].stations) {
                    true -> binding.stationsChip.isChecked = true
                    false -> binding.stationsChip.isChecked = false
                }

                when (currentFullProperty.pointsOfInterestList[0].university) {
                    true -> binding.universityChip.isChecked = true
                    false -> binding.universityChip.isChecked = false
                }

                val currentPropertyType = currentFullProperty.property.type
                propertyTypeSpinner.setSelection(getCurrentPropertyTypeAndCountryIndex(
                    propertyTypeSpinner,
                    currentPropertyType))

                val currentPropertyCountry = currentFullProperty.property.address.country
                propertyCountrySpinner.setSelection(getCurrentPropertyTypeAndCountryIndex(
                    propertyCountrySpinner,
                    currentPropertyCountry))

                viewModel.publicListOfMedia = currentFullProperty.mediaList as MutableList<Media>
                if (viewModel.publicListOfMedia.isNotEmpty() && viewModel.getMutableListOfMedia()
                        .isEmpty()
                ) {
                    viewModel.initListOfMedia()
                }

                val isSoldButton: SwitchMaterial = binding.isSoldSwitch
                isSoldButton.setOnClickListener(View.OnClickListener {
                    if (isSoldButton.isChecked) {
                        dateOfSale = Utils.getTodayDate()
                        isSold = true
                    } else {
                        isSold = false
                        dateOfSale = null
                    }
                })
                isSoldButton.isChecked = currentFullProperty.property.isSold == true
            }
        }

        val saveChangeButton: AppCompatButton = binding.saveButton
        saveChangeButton.setOnClickListener(View.OnClickListener {

            viewModel.updateProperty(Property(price = binding.priceEditText.text.toString()
                .toInt(),
                id = currentFullProperty.property.id,
                type = resultPropertyTypeSpinner,
                numberOfRooms = binding.roomsEditText.text.toString().toInt(),
                numberOfBedrooms = binding.bedroomsEditText.text.toString().toInt(),
                numberOfBathrooms = binding.bathroomsEditText.text.toString().toInt(),
                surface = binding.surfaceEditText.text.toString().toInt(),
                description = binding.descriptionEditText.text.toString(),
                address = Address(street = binding.streetEditText.text.toString(),
                    city = binding.cityEditText.text.toString(),
                    postcode = binding.postcodeEditText.text.toString().toInt(),
                    country = resultPropertyCountrySpinner),
                staticMapUri = currentFullProperty.property.staticMapUri,
                isSold = isSold,
                dateOfSale = dateOfSale,
                dateOfCreation = currentFullProperty.property.dateOfCreation))

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
                when (chipText) {
                    "School" -> schoolState = chipState
                    "University" -> universityState = chipState
                    "Parks" -> parksState = chipState
                    "Sports clubs" -> sportsClubsState = chipState
                    "Stations" -> stationsState = chipState
                    "Shopping centre" -> shoppingCentreState = chipState
                }
            }
            viewModel.updatePropertyPointOfInterest(
                PointsOfInterest(
                    propertyId = currentFullProperty.property.id,
                    id = currentFullProperty.property.id,
                    school = schoolState,
                    university = universityState,
                    parks = parksState,
                    sportsClubs = sportsClubsState,
                    stations = stationsState,
                    shoppingCenter = shoppingCentreState
                )
            )

            for (mediaToDelete in viewModel.listOfMediaToDelete) {
                context?.deleteFile(mediaToDelete.uri.substringAfterLast("/"))
                viewModel.deletePropertyMediaFromDb(mediaToDelete)
            }

            if (viewModel.getListOfMedia().value != null) {
                for (item in viewModel.getListOfMedia().value!!) {
                    if (!currentFullProperty.mediaList.contains(item)) {
                        if (!viewModel.mediaListToSet.contains(item)) {
                            viewModel.insertPropertyMedia(Media(uri = item.uri,
                                description = item.description,
                                propertyId = currentFullProperty.property.id))
                        }
                    }
                }
            }

            for (media in viewModel.mediaListToSet) {
                if (!viewModel.listOfMediaToDelete.contains(media)) {
                    viewModel.insertPropertyMedia(media)
                }
            }

            val navHostFragment =
                requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment_property_detail) as NavHostFragment
            navController = navHostFragment.navController
            navController.navigateUp()

        })

        fun updateSaveChangeButtonState() {
            RegexUtils.updateButtonState(saveChangeButton,
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
        updateSaveChangeButtonState()

        //PHOTO FROM GALLERY
        val getImageFromGalleryLauncher = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                val fileDate: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val fileName = "Photo"
                val uriPhoto: String = context?.filesDir.toString() + "/" + "$fileName$fileDate.jpg"
                if (it != null) {
                    val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(
                        context?.applicationContext?.contentResolver,
                        it
                    )
                    if (MediaUtils.savePhotoToInternalMemory(
                            fileDate,
                            fileName,
                            bitmap,
                            requireContext()
                        )
                    ) {
                        viewModel.addMedia(Media(propertyId = currentFullProperty.property.id,
                            uri = uriPhoto,
                            description = ""))
                    }
                }
            }
        )
        val galleryPhotoButton: AppCompatButton = binding.buttonForGalleryPhoto
        galleryPhotoButton.setOnClickListener(View.OnClickListener {
            getImageFromGalleryLauncher.launch("image/*")
        })

        //PHOTO FROM CAMERA
        val cameraPhotoButton: AppCompatButton = binding.buttonForCameraPhoto
        cameraPhotoButton.setOnClickListener(View.OnClickListener {
            val getImageFromCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            activityResultLauncher.launch(getImageFromCameraIntent)
        })
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult? ->
                if (result!!.resultCode == Activity.RESULT_OK) {
                    val fileDate: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                    val fileName = "Photo"
                    val uriPhoto: String =
                        context?.filesDir.toString() + "/" + "$fileName$fileDate.jpg"
                    val bitmap = result.data!!.extras!!.get("data") as Bitmap
                    if (MediaUtils.savePhotoToInternalMemory(
                            fileDate,
                            fileName,
                            bitmap,
                            requireContext()
                        )
                    ) {
                        viewModel.addMedia(Media(propertyId = currentFullProperty.property.id,
                            uri = uriPhoto,
                            description = ""))
                    }
                }
            }

        //VIDEO FROM CAMERA
        val cameraVideoButton: AppCompatButton = binding.buttonForCameraVideo
        cameraVideoButton.setOnClickListener(View.OnClickListener {
            val getVideoFromCameraIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            activityResultLauncherForVideo.launch(getVideoFromCameraIntent)
        })
        activityResultLauncherForVideo =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult? ->
                if (result!!.resultCode == Activity.RESULT_OK) {
                    viewModel.addMedia(
                        Media(
                            uri = result.data?.data.toString(),
                            description = "",
                            propertyId = currentFullProperty.property.id
                        )
                    )
                }
            }

        //VIDEO FROM GALlERY
        val galleryVideoButton: AppCompatButton = binding.buttonForGalleryVideo
        val getVideoFromGalleryLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent(), ActivityResultCallback {
                if (it != null) {
                    viewModel.addMedia(
                        Media(
                            uri = it.toString(),
                            description = "",
                            propertyId = currentFullProperty.property.id
                        )
                    )
                }
            })
        galleryVideoButton.setOnClickListener(View.OnClickListener {
            getVideoFromGalleryLauncher.launch("video/*")
        })

        viewModel.getListOfMedia().observe(viewLifecycleOwner, { mediaList ->
            setUpRecyclerView(recyclerView, mediaList)

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
                    Collections.swap(mediaList, fromPosition, toPosition)
                    recyclerView.adapter!!.notifyItemMoved(fromPosition, toPosition)
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    //SWIPE DELETE FEATURE
                }
            }
            val itemTouchHelper = ItemTouchHelper(simpleCallback)
            itemTouchHelper.attachToRecyclerView(recyclerView)

        })

        binding.priceEditText.addTextChangedListener {
            binding.price.helperText = RegexUtils.emptyTextVerification(binding.priceEditText.text)
            updateSaveChangeButtonState()
        }
        binding.priceEditText.setOnFocusChangeListener { _, _ ->
            binding.price.helperText = RegexUtils.emptyTextVerification(binding.priceEditText.text)
        }

        binding.roomsEditText.addTextChangedListener {
            binding.rooms.helperText = RegexUtils.emptyTextVerification(binding.roomsEditText.text)
            updateSaveChangeButtonState()
        }
        binding.roomsEditText.setOnFocusChangeListener { _, _ ->
            binding.rooms.helperText = RegexUtils.emptyTextVerification(binding.roomsEditText.text)
        }

        binding.bedroomsEditText.addTextChangedListener {
            binding.bedrooms.helperText =
                RegexUtils.emptyTextVerification(binding.bedroomsEditText.text)
            updateSaveChangeButtonState()
        }
        binding.bedroomsEditText.setOnFocusChangeListener { _, _ ->
            binding.bedrooms.helperText =
                RegexUtils.emptyTextVerification(binding.bedroomsEditText.text)
        }

        binding.bathroomsEditText.addTextChangedListener {
            binding.bathrooms.helperText =
                RegexUtils.emptyTextVerification(binding.bathroomsEditText.text)
            updateSaveChangeButtonState()
        }
        binding.bathroomsEditText.setOnFocusChangeListener { _, _ ->
            binding.bathrooms.helperText =
                RegexUtils.emptyTextVerification(binding.bathroomsEditText.text)
        }

        binding.surfaceEditText.addTextChangedListener {
            binding.surface.helperText =
                RegexUtils.emptyTextVerification(binding.surfaceEditText.text)
            updateSaveChangeButtonState()
        }
        binding.surfaceEditText.setOnFocusChangeListener { _, _ ->
            binding.surface.helperText =
                RegexUtils.emptyTextVerification(binding.surfaceEditText.text)
        }

        binding.descriptionEditText.addTextChangedListener {
            binding.description.helperText =
                RegexUtils.emptyTextVerification(binding.descriptionEditText.text)
            updateSaveChangeButtonState()
        }
        binding.descriptionEditText.setOnFocusChangeListener { _, _ ->
            binding.description.helperText =
                RegexUtils.emptyTextVerification(binding.descriptionEditText.text)
        }

        binding.cityEditText.addTextChangedListener {
            binding.city.helperText = RegexUtils.emptyTextVerification(binding.cityEditText.text)
            updateSaveChangeButtonState()
        }
        binding.cityEditText.setOnFocusChangeListener { _, _ ->
            binding.city.helperText = RegexUtils.emptyTextVerification(binding.cityEditText.text)
        }

        binding.postcodeEditText.addTextChangedListener {
            binding.postcode.helperText =
                RegexUtils.emptyTextVerification(binding.postcodeEditText.text)
            updateSaveChangeButtonState()
        }
        binding.postcodeEditText.setOnFocusChangeListener { _, _ ->
            binding.postcode.helperText =
                RegexUtils.emptyTextVerification(binding.postcodeEditText.text)
        }

        binding.streetEditText.addTextChangedListener {
            binding.street.helperText =
                RegexUtils.emptyTextVerification(binding.streetEditText.text)
            updateSaveChangeButtonState()
        }
        binding.streetEditText.setOnFocusChangeListener { _, _ ->
            binding.street.helperText =
                RegexUtils.emptyTextVerification(binding.streetEditText.text)
        }

        return rootView
    }

    private fun getCurrentPropertyTypeAndCountryIndex(
        propertyTypeSpinner: AppCompatSpinner,
        s: String?,
    ): Int {
        for (i in 0..propertyTypeSpinner.count) {
            if (propertyTypeSpinner.getItemAtPosition(i).toString().contentEquals(s)) {
                return i
            }
        }
        return 0
    }

    private fun setUpRecyclerView(recyclerView: RecyclerView, mediaList: List<Media>) {
        val myLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = myLayoutManager
        recyclerView.adapter = UpdatePropertyAdapter(mediaList, this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDescriptionPhotoChanged(description: String, uri: String) {
        viewModel.setDescriptionOfMedia(description, uri)
    }

    override fun onDeleteMedia(media: Media) {
        viewModel.deleteMedia(media)
        viewModel.listOfMediaToDelete.add(media)
    }

}