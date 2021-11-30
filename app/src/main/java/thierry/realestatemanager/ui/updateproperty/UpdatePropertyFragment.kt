package thierry.realestatemanager.ui.updateproperty

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.switchmaterial.SwitchMaterial
import dagger.hilt.android.AndroidEntryPoint
import thierry.realestatemanager.R
import thierry.realestatemanager.databinding.FragmentAddUpdatePropertyBinding


/**
 * A simple [Fragment] subclass.
 * Use the [UpdatePropertyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class UpdatePropertyFragment : Fragment() {
    private var _binding: FragmentAddUpdatePropertyBinding? = null
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
        _binding = FragmentAddUpdatePropertyBinding.inflate(inflater, container, false)
        val rootView = binding.root

        binding.title.text = getString(R.string.update_property)
        binding.price.isHelperTextEnabled = false
        binding.rooms.isHelperTextEnabled = false
        binding.bedrooms.isHelperTextEnabled = false
        binding.bathrooms.isHelperTextEnabled = false
        binding.surface.isHelperTextEnabled = false
        binding.description.isHelperTextEnabled = false
        binding.city.isHelperTextEnabled = false
        binding.postcode.isHelperTextEnabled = false
        binding.street.isHelperTextEnabled = false

        val isSoldButton: SwitchMaterial = binding.isSoldSwitch
        isSoldButton.setOnClickListener(View.OnClickListener {
            if (isSoldButton.isChecked) {
                Toast.makeText(requireContext(), "enabled", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "disabled", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.getCurrentProperty().observe(viewLifecycleOwner) { currentProperty ->
            binding.priceEditText.setText(currentProperty.price.toString())
            binding.roomsEditText.setText(currentProperty.numberOfRooms.toString())
            binding.bedroomsEditText.setText(currentProperty.numberOfBedrooms.toString())
            binding.bathroomsEditText.setText(currentProperty.numberOfBathrooms.toString())
            binding.surfaceEditText.setText(currentProperty.surface.toString())
            binding.descriptionEditText.setText(currentProperty.description)
            binding.cityEditText.setText(currentProperty.address!!.city)
            binding.postcodeEditText.setText(currentProperty.address.postcode.toString())
            binding.streetEditText.setText(currentProperty.address.street)
        }
        Log.i("YEAHH", "Yeahh + $viewModel.actualPropertyIndex.toString()")

        binding.priceEditText.addTextChangedListener {
            binding.price.helperText = validPriceText(binding.priceEditText.text)
        }
        binding.priceEditText.setOnFocusChangeListener { _, _ ->
            binding.price.helperText = validPriceText(binding.priceEditText.text)
        }

        binding.roomsEditText.addTextChangedListener {
            binding.rooms.helperText = validPriceText(binding.roomsEditText.text)
        }
        binding.roomsEditText.setOnFocusChangeListener { _, _ ->
            binding.rooms.helperText = validPriceText(binding.roomsEditText.text)
        }

        binding.bedroomsEditText.addTextChangedListener {
            binding.bedrooms.helperText = validPriceText(binding.bedroomsEditText.text)
        }
        binding.bedroomsEditText.setOnFocusChangeListener { _, _ ->
            binding.bedrooms.helperText = validPriceText(binding.bedroomsEditText.text)
        }

        binding.bathroomsEditText.addTextChangedListener {
            binding.bathrooms.helperText = validPriceText(binding.bathroomsEditText.text)
        }
        binding.bathroomsEditText.setOnFocusChangeListener { _, _ ->
            binding.bathrooms.helperText = validPriceText(binding.bathroomsEditText.text)
        }

        binding.surfaceEditText.addTextChangedListener {
            binding.surface.helperText = validPriceText(binding.surfaceEditText.text)
        }
        binding.surfaceEditText.setOnFocusChangeListener { _, _ ->
            binding.surface.helperText = validPriceText(binding.surfaceEditText.text)
        }

        binding.descriptionEditText.addTextChangedListener {
            binding.description.helperText = validPriceText(binding.descriptionEditText.text)
        }
        binding.descriptionEditText.setOnFocusChangeListener { _, _ ->
            binding.description.helperText = validPriceText(binding.descriptionEditText.text)
        }

        binding.cityEditText.addTextChangedListener {
            binding.city.helperText = validPriceText(binding.cityEditText.text)
        }
        binding.cityEditText.setOnFocusChangeListener { _, _ ->
            binding.city.helperText = validPriceText(binding.cityEditText.text)
        }

        binding.postcodeEditText.addTextChangedListener {
            binding.postcode.helperText = validPriceText(binding.postcodeEditText.text)
        }
        binding.postcodeEditText.setOnFocusChangeListener { _, _ ->
            binding.postcode.helperText = validPriceText(binding.postcodeEditText.text)
        }

        binding.streetEditText.addTextChangedListener {
            binding.street.helperText = validPriceText(binding.streetEditText.text)
        }
        binding.streetEditText.setOnFocusChangeListener { _, _ ->
            binding.street.helperText = validPriceText(binding.streetEditText.text)
        }

        return rootView
    }

    private fun validPriceText(textEditText: Editable?): String? {
        return when (textEditText.toString()) {
            "" -> "Field can't be empty"
            else -> return null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}