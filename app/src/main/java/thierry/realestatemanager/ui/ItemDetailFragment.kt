package thierry.realestatemanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import thierry.realestatemanager.databinding.FragmentItemDetailBinding
import thierry.realestatemanager.model.Property

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [ItemListFragment]
 * in two-pane mode (on larger screen devices) or self-contained
 * on handsets.
 */
@AndroidEntryPoint
class ItemDetailFragment : Fragment() {

    private val viewModel: ItemDetailViewModel by viewModels()

    /**
     * The placeholder content this fragment is presenting.
     */

    private var item: String? = null


    private var _binding: FragmentItemDetailBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                // Load the placeholder content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                item = it.getString(ARG_ITEM_ID)

            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentItemDetailBinding.inflate(inflater, container, false)
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


        viewModel.allProperty.observe(viewLifecycleOwner) { listOfProperty ->

            val property: Property? = listOfProperty.find { it.id.toString() == item }

            if (property != null) {
                propertyDescription!!.text = "How do you know exactly what you're buying when it comes to land? A legal description is a written record of a piece of land containing information that clearly identifies it. This description can be written in a few different forms depending on where the property is located in the United States. However, when dealing with one geographic area, the descriptions will tend to follow the same wording and language.A legal description provides legal evidence of the boundaries and allows a surveyor to accurately determine property lines at a later time. This is incredibly useful and necessary during real estate transactions or disputes. The description will typically appear on sales contracts and the property deed."
                mediaTitle!!.text = "Media"
                propertyDescriptionTitle!!.text = "Description"
                propertySurface!!.text = "Surface"
                propertySurfaceValue!!.text = "750 sq m"
                numberOfRooms!!.text = "Number of rooms"
                numberOfRoomsValue!!.text = "8"
                numberOfBathrooms!!.text = "Number of bathrooms"
                numberOfBathroomsValue!!.text = "2"
                numberOfBedrooms!!.text = "Number of bedrooms"
                numberOfBedroomsValue!!.text = "4"
            }

        }

        val listOfPropertyPhoto = listOf<Property>(Property(price = 12, type = "Penthouse"),Property(price = 15, type = "Penthouse"))
        recyclerView?.let { setUpRecyclerView(it, listOfPropertyPhoto) }

        return rootView
    }

    private fun setUpRecyclerView(recyclerView: RecyclerView, listOfPropertyPhoto: Any) {
        val myLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = myLayoutManager
        recyclerView.adapter = ItemDetailAdapter()
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
}