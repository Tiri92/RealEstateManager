package thierry.realestatemanager.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import thierry.realestatemanager.R
import thierry.realestatemanager.databinding.FragmentAddAndUpdatePropertyBinding
import thierry.realestatemanager.databinding.FragmentItemDetailBinding
import thierry.realestatemanager.model.Property

/**
 * A simple [Fragment] subclass.
 * Use the [AddAndUpdateProperty.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddAndUpdateProperty : Fragment() {

    private var _binding: FragmentAddAndUpdatePropertyBinding? = null


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

        val recyclerView: RecyclerView = binding.recyclerviewFragmentAddAndUpdate
        val listOfPropertyPhoto = listOf<Property>(Property(price = 12), Property(price = 15))
        recyclerView?.let { setUpRecyclerView(it, listOfPropertyPhoto) }


        return rootView
    }

    private fun setUpRecyclerView(recyclerView: RecyclerView, listOfPropertyPhoto: Any) {
        val myLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView!!.layoutManager = myLayoutManager
        recyclerView.adapter = AddAndUpdateAdapter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}