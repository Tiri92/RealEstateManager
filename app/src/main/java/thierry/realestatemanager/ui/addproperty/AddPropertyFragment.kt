package thierry.realestatemanager.ui.addproperty

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatSpinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import thierry.realestatemanager.R
import thierry.realestatemanager.databinding.FragmentAddPropertyBinding
import thierry.realestatemanager.model.Address
import thierry.realestatemanager.model.Media
import thierry.realestatemanager.model.Property
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddPropertyFragment : AddPropertyAdapter.PhotoDescriptionChanged, Fragment() {

    private val viewModel: AddPropertyViewModel by viewModels()
    private var _binding: FragmentAddPropertyBinding? = null
    private val binding get() = _binding!!
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var activityResultLauncherForVideo: ActivityResultLauncher<Intent>
    private var listOfMediaToSave = mutableListOf<Media>()
    lateinit var resultPropertyTypeSpinner: String
    lateinit var resultPropertyCountrySpinner: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPropertyBinding.inflate(inflater, container, false)
        val rootView = binding.root

        val recyclerView: RecyclerView = binding.recyclerviewFragmentAddAndUpdate
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
                p3: Long
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
                    p3: Long
                ) {
                    val cSpinnerResult: String = parent?.getItemAtPosition(position).toString()
                    resultPropertyCountrySpinner = cSpinnerResult
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }

        fun chipsTest() {
            val valChipGroupMulti: ChipGroup? = binding.chipGroupMulti
            valChipGroupMulti?.checkedChipIds?.forEach {
                val chip = binding.chipGroupMulti.findViewById<Chip>(it).text.toString()
                Log.i("[CHIP]", "chip $chip.")
            }
        }

        viewModel.getLastIdPropertyTable.observe(viewLifecycleOwner) {
            val lastIndex: Int = it

            val saveButton: AppCompatButton = binding.saveButton
            saveButton.setOnClickListener(View.OnClickListener {
                viewModel.insertProperty(
                    Property(
                        price = binding.priceEditText.text.toString().toInt(),
                        type = resultPropertyTypeSpinner,
                        address = Address(
                            city = resultPropertyCountrySpinner,
                            street = binding.streetEditText.text.toString()
                        ),
                        numberOfRooms = binding.roomsEditText.text.toString().toInt(),
                        numberOfBedrooms = binding.bedroomsEditText.text.toString().toInt(),
                        numberOfBathrooms = binding.bathroomsEditText.text.toString().toInt(),
                        surface = binding.surfaceEditText.text.toString().toInt(),
                        description = binding.descriptionEditText.text.toString()
                    )
                )
                for (item in listOfMediaToSave) {
                    viewModel.insertMedia(
                        Media(
                            uri = item.uri,
                            propertyId = lastIndex,
                            description = item.description
                        )
                    )
                }
                chipsTest()
            })
        }

        //PHOTO FROM GALLERY
        val getImageFromGalleryLauncher = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                val fileName: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                if (it != null) {
                    val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(
                        context?.applicationContext?.contentResolver,
                        it
                    )
                    savePhotoToInternalMemory("Photo_$fileName", bitmap, recyclerView)
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
                    val bitmap = result.data!!.extras!!.get("data") as Bitmap
                    val fileName: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                    savePhotoToInternalMemory("Photo_$fileName", bitmap, recyclerView)
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
                    listOfMediaToSave.add(
                        Media(
                            uri = result.data?.data.toString(),
                            description = "ça marche",
                            propertyId = 2
                        )
                    )
                    recyclerView.adapter!!.notifyDataSetChanged()
                }
            }

        //VIDEO FROM GALlERY
        val galleryVideoButton: AppCompatButton = binding.buttonForGalleryVideo
        val getVideoFromGalleryLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent(), ActivityResultCallback {
                if (it != null) {
                    listOfMediaToSave.add(
                        Media(
                            uri = it.toString(),
                            description = "ça marche",
                            propertyId = 2
                        )
                    )
                    recyclerView.adapter!!.notifyDataSetChanged()
                }
            })
        galleryVideoButton.setOnClickListener(View.OnClickListener {
            getVideoFromGalleryLauncher.launch("video/*")
        })

        setUpRecyclerView(recyclerView, listOfMediaToSave)

        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.edit).isVisible = false
        menu.findItem(R.id.add).isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setUpRecyclerView(
        recyclerView: RecyclerView,
        listOfPropertyMedia: MutableList<Media>
    ) {
        val myLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = myLayoutManager
        recyclerView.adapter = AddPropertyAdapter(listOfPropertyMedia, this)
    }

    private fun savePhotoToInternalMemory(
        filename: String,
        bmp: Bitmap,
        recyclerView: RecyclerView
    ): Boolean {
        return try {
            context?.openFileOutput("$filename.jpg", Activity.MODE_PRIVATE).use { stream ->

                //compress photo
                if (!bmp.compress(Bitmap.CompressFormat.JPEG, 95, stream)) {
                    throw IOException("erreur compression")
                }
                val uriPhoto: String = context?.filesDir.toString() + "/" + "$filename.jpg"
                listOfMediaToSave.add(Media(propertyId = 2, uri = uriPhoto, description = ""))
                recyclerView.adapter!!.notifyDataSetChanged()

            }
            true

        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDescriptionPhotoChanged(description: String, uri: String) {
        listOfMediaToSave.find { it.uri == uri }?.description = description
    }

}