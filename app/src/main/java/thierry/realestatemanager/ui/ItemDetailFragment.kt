package thierry.realestatemanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.appbar.CollapsingToolbarLayout
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

    private lateinit var itemDetailTextView: TextView
    private var toolbarLayout: CollapsingToolbarLayout? = null

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

        viewModel.allProperty.observe(viewLifecycleOwner) { listOfProperty ->

            val property: Property? = listOfProperty.find { it.id.toString() == item }

            if (property != null) {
                toolbarLayout = binding.toolbarLayout
                itemDetailTextView = binding.itemDetail
                updateContent(property.price.toString())
            }

        }

        return rootView
    }

    private fun updateContent(text: String) {
        toolbarLayout?.title = item

        // Show the placeholder content as text in a TextView.
        item?.let {
            itemDetailTextView.text = text
        }
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