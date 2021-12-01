package thierry.realestatemanager.ui.propertydetail

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
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
import thierry.realestatemanager.model.Media
import thierry.realestatemanager.model.Property
import thierry.realestatemanager.model.PropertyWithMedia
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [PropertyListFragment]
 * in two-pane mode (on larger screen devices) or self-contained
 * on handsets.
 */
@AndroidEntryPoint
class PropertyDetailFragment : StaticMapRequestListener.Callback, Fragment() {

    private val viewModel: PropertyDetailViewModel by viewModels()

    /**
     * The placeholder content this fragment is presenting.
     */

    private var item: String? = null


    private var _binding: FragmentPropertyDetailBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var staticMap: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                // Load the placeholder content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                item = it.getString(ARG_ITEM_ID)
                viewModel.setCurrentPropertyId(item.toString().toInt())
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (item != "") {
            menu.findItem(R.id.edit).isVisible = true
        } else {
            menu.findItem(R.id.edit).isVisible = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPropertyDetailBinding.inflate(inflater, container, false)
        val rootView = binding.root

        val recyclerView: RecyclerView? = binding.recyclerviewFragmentDetail

        val mediaTitle: TextView? = binding.mediaTitle
        val propertyDescriptionTitle: TextView? = binding.propertyDescriptionTitle
        val propertyDescription: TextView? = binding.propertyDescription
        val propertySurface: TextView? = binding.propertySurface
        val propertySurfaceValue: TextView? = binding.propertySurfaceValue
        val numberOfRooms: TextView? = binding.numberOfRooms
        val numberOfRoomsValue: TextView? = binding.numberOfRoomsValue
        val numberOfBathrooms: TextView? = binding.numberOfBathrooms
        val numberOfBathroomsValue: TextView? = binding.numberOfBathroomsValue
        val numberOfBedrooms: TextView? = binding.numberOfBedrooms
        val numberOfBedroomsValue: TextView? = binding.numberOfBedroomsValue


        viewModel.allPropertyPhoto.observe(viewLifecycleOwner) { listOfProperty ->

            val property: PropertyWithMedia? =
                listOfProperty.find { it.property.id.toString() == item }

            if (property != null) {
                propertyDescription!!.text = property.property.description
                mediaTitle!!.text = "Media"
                propertyDescriptionTitle!!.text = "Description"
                propertySurface!!.text = "Surface"
                propertySurfaceValue!!.text = property.property.surface.toString()
                numberOfRooms!!.text = "Number of rooms"
                numberOfRoomsValue!!.text = property.property.numberOfRooms.toString()
                numberOfBathrooms!!.text = "Number of bathrooms"
                numberOfBathroomsValue!!.text = property.property.numberOfBathrooms.toString()
                numberOfBedrooms!!.text = "Number of bedrooms"
                numberOfBedroomsValue!!.text = property.property.numberOfBedrooms.toString()
                var address: String =
                    property.property.address!!.city + "+" + property.property.address!!.postcode

                val listOfPropertyMedia: List<Media> = property.mediaList
                recyclerView?.let { setUpRecyclerView(it, listOfPropertyMedia) }

                staticMap = binding.staticMap!!
                val staticMapUri =
                    "https://maps.googleapis.com/maps/api/staticmap?center=" + address + "&zoom=15&size=600x300&maptype=roadmap" + "&key=" + BuildConfig.GOOGLE_KEY

                if (property.property.staticMapUri != null) {
                    Glide.with(this).load(property.property.staticMapUri).centerCrop()
                        .into(staticMap)
                } else {
                    Glide.with(this).load(staticMapUri).centerCrop()
                        .listener(StaticMapRequestListener(this))/*.error(R.drawable.ERRORPHOTO)*/
                        .into(staticMap)
                }

                viewModel.currentProperty = property.property

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
        Log.i("NOOO", "NOOO")
    }

    override fun onSuccess(dataSource: Drawable) {
        val staticMapBitmap = dataSource.toBitmap()
        val fileName: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        savePhotoToInternalMemory(fileName, staticMapBitmap)

    }

    private fun savePhotoToInternalMemory(
        filename: String,
        bmp: Bitmap
    ): Boolean {
        return try {
            context?.openFileOutput("StaticMapPhoto$filename.jpg", Activity.MODE_PRIVATE)
                .use { stream ->

                    //compress photo
                    if (!bmp.compress(Bitmap.CompressFormat.JPEG, 95, stream)) {
                        throw IOException("erreur compression")
                    }
                    val uriStaticMapPhoto: String =
                        context?.filesDir.toString() + "/" + "StaticMapPhoto$filename.jpg"
                    viewModel.updateProperty(
                        Property(
                            id = item!!.toInt(),
                            price = viewModel.currentProperty.price,
                            type = viewModel.currentProperty.type,
                            surface = viewModel.currentProperty.surface,
                            description = viewModel.currentProperty.description,
                            numberOfRooms = viewModel.currentProperty.numberOfRooms,
                            numberOfBathrooms = viewModel.currentProperty.numberOfBathrooms,
                            numberOfBedrooms = viewModel.currentProperty.numberOfBedrooms,
                            address = viewModel.currentProperty.address,
                            staticMapUri = uriStaticMapPhoto,
                            isSold = viewModel.currentProperty.isSold
                        )
                    )

                }
            true

        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }
}