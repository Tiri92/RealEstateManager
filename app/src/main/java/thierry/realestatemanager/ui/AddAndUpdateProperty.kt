package thierry.realestatemanager.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import thierry.realestatemanager.databinding.FragmentAddAndUpdatePropertyBinding
import thierry.realestatemanager.model.Property
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [AddAndUpdateProperty.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddAndUpdateProperty : Fragment() {

    private var _binding: FragmentAddAndUpdatePropertyBinding? = null
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

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
        _binding = FragmentAddAndUpdatePropertyBinding.inflate(inflater, container, false)
        val rootView = binding.root

        //GALLERY
        val imageView: ImageView = binding.imageview
        val getImage = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                imageView.setImageURI(it)
                val fileName: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(
                    context?.applicationContext?.contentResolver,
                    it
                )
                if (bitmap != null) {
                    savePhotoToInternalMemory("Photo_$fileName", bitmap)
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

        val recyclerView: RecyclerView = binding.recyclerviewFragmentAddAndUpdate
        val listOfPropertyPhoto = listOf<Property>(Property(price = 12), Property(price = 15))
        recyclerView?.let { setUpRecyclerView(it, listOfPropertyPhoto) }


        return rootView
    }

    private fun setUpRecyclerView(recyclerView: RecyclerView, listOfPropertyPhoto: Any) {
        val myLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView!!.layoutManager = myLayoutManager
        recyclerView.adapter = AddAndUpdateAdapter()
    }

    private fun savePhotoToInternalMemory(filename: String, bmp: Bitmap): Boolean {
        return try {
            context?.openFileOutput("$filename.jpg", Activity.MODE_PRIVATE).use { stream ->

                //compress photo
                if (!bmp.compress(Bitmap.CompressFormat.JPEG, 95, stream)) {
                    throw IOException("erreur compression")
                }
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