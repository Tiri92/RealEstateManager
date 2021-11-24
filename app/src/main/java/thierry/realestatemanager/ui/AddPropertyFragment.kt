package thierry.realestatemanager.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.*
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
import com.google.android.material.switchmaterial.SwitchMaterial
import dagger.hilt.android.AndroidEntryPoint
import thierry.realestatemanager.R
import thierry.realestatemanager.databinding.FragmentAddPropertyBinding
import thierry.realestatemanager.model.Address
import thierry.realestatemanager.model.Photo
import thierry.realestatemanager.model.Property
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [AddPropertyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class AddPropertyFragment : Fragment() {

    private val viewModel: AddPropertyViewModel by viewModels()
    private var _binding: FragmentAddPropertyBinding? = null
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private var listOfPhotoToSave = mutableListOf<String>()
    lateinit var textS:String
    lateinit var textS2:String

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddPropertyBinding.inflate(inflater, container, false)
        val rootView = binding.root

        setHasOptionsMenu(true)

        val spinner: AppCompatSpinner = binding.typeOfPropertySpinner
        val adapter = ArrayAdapter.createFromResource(requireContext(), R.array.PropertiesTypes, android.R.layout.simple_spinner_item )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                val text: String = parent?.getItemAtPosition(position).toString()
                textS = text
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        val spinnerC: AppCompatSpinner = binding.countrySpinner
        val adapterC = ArrayAdapter.createFromResource(requireContext(), R.array.Countries, android.R.layout.simple_spinner_item )
        adapterC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerC.adapter = adapterC
        spinnerC.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                val text: String = parent?.getItemAtPosition(position).toString()
                textS2 = text
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        val isSoldButton: SwitchMaterial = binding.isSoldSwitch
        isSoldButton.setOnClickListener(View.OnClickListener {
            if(isSoldButton.isChecked) {
                Toast.makeText(requireContext(), "enabled", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "disabled", Toast.LENGTH_SHORT).show()
            }
        })

        var saveButton: AppCompatButton = binding.saveButton
        saveButton.setOnClickListener(View.OnClickListener {
            viewModel.insertProperty(Property(price = binding.priceEditText.text.toString().toInt(), type = textS, address = Address(city = textS2, street = "31 Rue de l'égalité")))
            for(item in listOfPhotoToSave) {
                viewModel.insertPhoto(Photo(uri = item, propertyId = 2, photoName = "ça marhce"))
            }
        })

        //GALLERY
        val imageView: ImageView = binding.imageview
        val getImage = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                imageView.setImageURI(it)
                val fileName: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                if (it != null) {
                    val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(
                        context?.applicationContext?.contentResolver,
                        it
                    )
                    if (bitmap != null) {
                        savePhotoToInternalMemory("Photo_$fileName", bitmap)
                    }
                }
            }
        )
        val photoButton: AppCompatButton = binding.buttonForMainPhoto
        photoButton.setOnClickListener(View.OnClickListener {
            getImage.launch("image/*")
        })

        //CAMERA
        val cameraButton: AppCompatButton = binding.buttonForMainPhotoCamera
        cameraButton.setOnClickListener(View.OnClickListener {
            var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            activityResultLauncher.launch(intent)
        })
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult? ->
                if (result!!.resultCode == Activity.RESULT_OK) {
                    var bitmap = result!!.data!!.extras!!.get("data") as Bitmap
                    binding.imageview.setImageBitmap(bitmap)
                    val fileName: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                    savePhotoToInternalMemory("Photo_$fileName", bitmap)
                } else {
                }
            }

//        val recyclerView: RecyclerView = binding.recyclerviewFragmentAddAndUpdate
//        viewModel.allPropertyPhoto.observe(viewLifecycleOwner) { propertyPhoto ->
//            var listOfPropertyPhoto: List<Photo> = propertyPhoto[0].Photolist
//
//            recyclerView?.let { setUpRecyclerView(it, listOfPropertyPhoto) }
//        }

        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.edit).isVisible = false
        menu.findItem(R.id.add).isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setUpRecyclerView(recyclerView: RecyclerView, listOfPropertyPhoto: List<Photo>) {
        val myLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView!!.layoutManager = myLayoutManager
        recyclerView.adapter = AddAndUpdateAdapter(listOfPropertyPhoto)
    }

    private fun savePhotoToInternalMemory(filename: String, bmp: Bitmap): Boolean {
        return try {
            context?.openFileOutput("$filename.jpg", Activity.MODE_PRIVATE).use { stream ->

                //compress photo
                if (!bmp.compress(Bitmap.CompressFormat.JPEG, 95, stream)) {
                    throw IOException("erreur compression")
                }
                val uriPhoto: String = context?.filesDir.toString() + "/" + "$filename.jpg"
                listOfPhotoToSave.add(uriPhoto)
                viewModel.insertPhoto(
                    photo = Photo(
                        propertyId = 1,
                        uri = uriPhoto,
                        photoName = "nomdelaphoto"
                    )
                )
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

}