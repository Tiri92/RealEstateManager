package thierry.realestatemanager.ui.updateproperty

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.android.material.switchmaterial.SwitchMaterial
import dagger.hilt.android.AndroidEntryPoint
import thierry.realestatemanager.databinding.FragmentUpdatePropertyBinding
import thierry.realestatemanager.ui.addproperty.AddPropertyViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [UpdatePropertyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class UpdatePropertyFragment : Fragment() {
    private var _binding: FragmentUpdatePropertyBinding? = null
    private val viewModel by viewModels<UpdatePropertyViewModel>()

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
        _binding = FragmentUpdatePropertyBinding.inflate(inflater, container, false)
        val rootView = binding.root

        val isSoldButton: SwitchMaterial = binding.isSoldSwitchU
        isSoldButton.setOnClickListener(View.OnClickListener {
            if(isSoldButton.isChecked) {
                Toast.makeText(requireContext(), "enabled", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "disabled", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.getActualProperty().observe(viewLifecycleOwner) {
            binding.priceEditTextU.setText(it.id.toString())
        }
        Log.i("YEAHH","Yeahh + $viewModel.actualPropertyIndex.toString()")

        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}