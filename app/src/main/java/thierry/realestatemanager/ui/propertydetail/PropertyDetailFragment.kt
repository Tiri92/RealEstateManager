package thierry.realestatemanager.ui.propertydetail

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import thierry.realestatemanager.BuildConfig
import thierry.realestatemanager.R
import thierry.realestatemanager.databinding.FragmentPropertyDetailBinding
import thierry.realestatemanager.model.FullProperty
import thierry.realestatemanager.model.Media
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class PropertyDetailFragment : StaticMapRequestListener.Callback, Fragment() {

    private val viewModel: PropertyDetailViewModel by viewModels()
    private var currentPropertyId: String? = null
    private var _binding: FragmentPropertyDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var staticMap: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                currentPropertyId = it.getString(ARG_ITEM_ID)
                if (currentPropertyId != "") {
                    viewModel.setCurrentPropertyId(currentPropertyId.toString().toInt())
                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        when (currentPropertyId != "") {
            true -> menu.findItem(R.id.edit).isVisible = true
            else -> menu.findItem(R.id.edit).isVisible = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPropertyDetailBinding.inflate(inflater, container, false)
        val rootView = binding.root
        val recyclerView: RecyclerView = binding.recyclerviewFragmentDetail

        viewModel.getFullPropertyList.observe(viewLifecycleOwner) { fullPropertyList ->

            val currentFullProperty: FullProperty =
                fullPropertyList.find { fullPropertyListItem -> fullPropertyListItem.property.id.toString() == currentPropertyId }!!

            if (fullPropertyList != null) {
                binding.propertyDescription.text = currentFullProperty.property.description
                binding.mediaTitle.text = getString(R.string.media)
                binding.propertyDescriptionTitle.text = getString(R.string.description)
                binding.propertySurface.text = getString(R.string.surface)
                binding.propertySurfaceValue.text = currentFullProperty.property.surface.toString()
                binding.numberOfRooms.text = getString(R.string.number_rooms)
                binding.numberOfRoomsValue.text =
                    currentFullProperty.property.numberOfRooms.toString()
                binding.numberOfBathrooms.text = getString(R.string.number_bathrooms)
                binding.numberOfBathroomsValue.text =
                    currentFullProperty.property.numberOfBathrooms.toString()
                binding.numberOfBedrooms.text = getString(R.string.number_bedrooms)
                binding.numberOfBedroomsValue.text =
                    currentFullProperty.property.numberOfBedrooms.toString()

                staticMap = binding.staticMap
                val currentPropertyAddress: String =
                    currentFullProperty.property.address!!.city + "+" + currentFullProperty.property.address.postcode
                val staticMapUri =
                    "https://maps.googleapis.com/maps/api/staticmap?center=" + currentPropertyAddress + "&zoom=15&size=600x300&maptype=roadmap" + "&key=" + BuildConfig.MAPS_API_KEY

                if (currentFullProperty.property.staticMapUri != null) {
                    Glide.with(this).load(currentFullProperty.property.staticMapUri).centerCrop()
                        .into(staticMap)
                } else {
                    Glide.with(this).load(staticMapUri).centerCrop()
                        .listener(StaticMapRequestListener(this))/*.error(R.drawable.ERRORPHOTO)*/
                        .into(staticMap)
                }

                viewModel.currentProperty = currentFullProperty.property
                setUpRecyclerView(recyclerView, currentFullProperty.mediaList)
            }
        }

        return rootView
    }

    private fun setUpRecyclerView(recyclerView: RecyclerView, listOfPropertyMedia: List<Media>) {
        val myLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = myLayoutManager
        recyclerView.adapter = PropertyDetailAdapter(listOfPropertyMedia)
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onFailure(message: String?) {
    }

    override fun onSuccess(dataSource: Drawable) {
        val staticMapBitmap = dataSource.toBitmap()
        val fileDate: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        savePhotoToInternalMemory(fileDate, staticMapBitmap)
    }

    private fun savePhotoToInternalMemory( // Cree une class MEDIA UTILS, la mettre dans un COMPANION comme static pour eviter les new
        fileDate: String,
        bitmap: Bitmap
    ): Boolean {
        return try {
            val fileName = "StaticMapPhoto"
            context?.openFileOutput("$fileName$fileDate.jpg", Activity.MODE_PRIVATE)
                .use { stream ->

                    //compress photo
                    if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 95, stream)) {
                        throw IOException("error compression")
                    }
                    val uriStaticMapPhoto: String =
                        context?.filesDir.toString() + "/" + "$fileName$fileDate.jpg"
                    viewModel.currentProperty.staticMapUri = uriStaticMapPhoto
                    viewModel.updateProperty(viewModel.currentProperty)
                }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

}