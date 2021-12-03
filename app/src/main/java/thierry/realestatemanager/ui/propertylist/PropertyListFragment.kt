package thierry.realestatemanager.ui.propertylist

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import thierry.realestatemanager.R
import thierry.realestatemanager.databinding.FragmentPropertyListBinding
import thierry.realestatemanager.model.FullProperty
import thierry.realestatemanager.model.Property
import thierry.realestatemanager.ui.propertydetail.PropertyDetailFragment

@AndroidEntryPoint
class PropertyListFragment : Fragment() {

    private val viewModel: PropertyListViewModel by viewModels()
    private var _binding: FragmentPropertyListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        setHasOptionsMenu(true)

        _binding = FragmentPropertyListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.findItem(R.id.edit).isVisible = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = binding.recyclerviewFragmentList

        // Leaving this not using view binding as it relies on if the view is visible the current
        // layout configuration (layout, layout-sw600dp)
        val propertyDetailFragmentContainer: View? =
            view.findViewById(R.id.item_detail_nav_container)

        /** Click Listener to trigger navigation based on if you have
         * a single pane layout or two pane layout
         */
        val onClickListener = View.OnClickListener { itemView ->
            val item = itemView.tag
            val bundle = Bundle()
            bundle.putString(
                PropertyDetailFragment.ARG_ITEM_ID,
                item.toString()
            )
            if (propertyDetailFragmentContainer != null) {
                propertyDetailFragmentContainer.findNavController()
                    .navigate(R.id.fragment_property_detail, bundle)
            } else {
                itemView.findNavController().navigate(R.id.show_item_detail, bundle)
            }
        }

        /**
         * Context click listener to handle Right click events
         * from mice and trackpad input to provide a more native
         * experience on larger screen devices
         */
        val onContextClickListener = View.OnContextClickListener { v ->
            val item = v.tag
            Toast.makeText(
                v.context,
                "Context click of item $item",
                Toast.LENGTH_LONG
            ).show()
            true
        }

        viewModel.getFullPropertyList.observe(viewLifecycleOwner) { fullPropertyList ->
            setupRecyclerView(
                recyclerView,
                onClickListener,
                onContextClickListener,
                fullPropertyList
            )
        }
    }

    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        onClickListener: View.OnClickListener,
        onContextClickListener: View.OnContextClickListener,
        listOfProperty: List<FullProperty>
    ) {

        recyclerView.adapter = PropertyListAdapter(
            listOfProperty,
            onClickListener,
            onContextClickListener
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}