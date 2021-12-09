package thierry.realestatemanager.ui.addproperty

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import thierry.realestatemanager.R
import thierry.realestatemanager.databinding.FragmentAddUpdatePropertyBinding
import thierry.realestatemanager.model.Address
import thierry.realestatemanager.model.Media
import thierry.realestatemanager.model.PointsOfInterest
import thierry.realestatemanager.model.Property
import thierry.realestatemanager.utils.MediaUtils
import thierry.realestatemanager.utils.RegexUtils
import java.text.SimpleDateFormat
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddUpdatePropertyBinding.inflate(inflater, container, false)
        val rootView = binding.root

        val recyclerView: RecyclerView = binding.recyclerviewFragmentAddAndUpdate
        binding.isSoldSwitch.isVisible = false
        setHasOptionsMenu(true)

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

            val saveButton: AppCompatButton = binding.saveButton
            saveButton.setOnClickListener(View.OnClickListener {

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
                viewModel.insertPointsOfInterest(
                    PointsOfInterest(
                        propertyId = lastIndexValue,
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
                        description = binding.descriptionEditText.text.toString()
                    )
                )
                for (item in viewModel.getListOfMedia().value!!) {
                    viewModel.insertMedia(
                        Media(
                            uri = item.uri,
                            propertyId = lastIndexValue,
                            description = item.description
                        )
                    )
                }

            })
        }

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
                        viewModel.addMedia(Media(propertyId = 2, uri = uriPhoto, description = ""))
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
                        viewModel.addMedia(Media(propertyId = 2, uri = uriPhoto, description = ""))
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
                            propertyId = 2
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
                            propertyId = 2
                        )
                    )
                }
            })
        galleryVideoButton.setOnClickListener(View.OnClickListener {
            getVideoFromGalleryLauncher.launch("video/*")
        })

        viewModel.getListOfMedia().observe(viewLifecycleOwner, {
            setUpRecyclerView(recyclerView, it)
        })

        binding.priceEditText.addTextChangedListener {
            binding.price.helperText = RegexUtils.validPriceText(binding.priceEditText.text)
        }
        binding.priceEditText.setOnFocusChangeListener { _, _ ->
            binding.price.helperText = RegexUtils.validPriceText(binding.priceEditText.text)
        }

        binding.roomsEditText.addTextChangedListener {
            binding.rooms.helperText = RegexUtils.validPriceText(binding.roomsEditText.text)
        }
        binding.roomsEditText.setOnFocusChangeListener { _, _ ->
            binding.rooms.helperText = RegexUtils.validPriceText(binding.roomsEditText.text)
        }

        binding.bedroomsEditText.addTextChangedListener {
            binding.bedrooms.helperText = RegexUtils.validPriceText(binding.bedroomsEditText.text)
        }
        binding.bedroomsEditText.setOnFocusChangeListener { _, _ ->
            binding.bedrooms.helperText = RegexUtils.validPriceText(binding.bedroomsEditText.text)
        }

        binding.bathroomsEditText.addTextChangedListener {
            binding.bathrooms.helperText = RegexUtils.validPriceText(binding.bathroomsEditText.text)
        }
        binding.bathroomsEditText.setOnFocusChangeListener { _, _ ->
            binding.bathrooms.helperText = RegexUtils.validPriceText(binding.bathroomsEditText.text)
        }

        binding.surfaceEditText.addTextChangedListener {
            binding.surface.helperText = RegexUtils.validPriceText(binding.surfaceEditText.text)
        }
        binding.surfaceEditText.setOnFocusChangeListener { _, _ ->
            binding.surface.helperText = RegexUtils.validPriceText(binding.surfaceEditText.text)
        }

        binding.descriptionEditText.addTextChangedListener {
            binding.description.helperText =
                RegexUtils.validPriceText(binding.descriptionEditText.text)
        }
        binding.descriptionEditText.setOnFocusChangeListener { _, _ ->
            binding.description.helperText =
                RegexUtils.validPriceText(binding.descriptionEditText.text)
        }

        binding.cityEditText.addTextChangedListener {
            binding.city.helperText = RegexUtils.validCityText(binding.cityEditText.text)
        }
        binding.cityEditText.setOnFocusChangeListener { _, _ ->
            binding.city.helperText = RegexUtils.validCityText(binding.cityEditText.text)
        }

        binding.postcodeEditText.addTextChangedListener {
            binding.postcode.helperText = RegexUtils.validPriceText(binding.postcodeEditText.text)
        }
        binding.postcodeEditText.setOnFocusChangeListener { _, _ ->
            binding.postcode.helperText = RegexUtils.validPriceText(binding.postcodeEditText.text)
        }

        binding.streetEditText.addTextChangedListener {
            binding.street.helperText = RegexUtils.validPriceText(binding.streetEditText.text)
        }
        binding.streetEditText.setOnFocusChangeListener { _, _ ->
            binding.street.helperText = RegexUtils.validPriceText(binding.streetEditText.text)
        }

        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.edit).isVisible = false
        menu.findItem(R.id.add).isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setUpRecyclerView(
        recyclerView: RecyclerView,
        listOfPropertyMedia: List<Media>,
    ) {
        val myLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = myLayoutManager
        recyclerView.adapter = AddPropertyAdapter(listOfPropertyMedia, this)
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

}