package thierry.realestatemanager.ui.updateproperty

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.switchmaterial.SwitchMaterial
import dagger.hilt.android.AndroidEntryPoint
import thierry.realestatemanager.R
import thierry.realestatemanager.databinding.FragmentAddUpdatePropertyBinding
import thierry.realestatemanager.model.Media

@AndroidEntryPoint
class UpdatePropertyFragment : UpdatePropertyAdapter.PhotoDescriptionChanged, Fragment() {

    private var _binding: FragmentAddUpdatePropertyBinding? = null
    private val viewModel by viewModels<UpdatePropertyViewModel>()
    private val binding get() = _binding!!
    lateinit var resultPropertyTypeSpinner: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddUpdatePropertyBinding.inflate(inflater, container, false)
        val rootView = binding.root
        val recyclerView: RecyclerView = binding.recyclerviewFragmentAddAndUpdate

        binding.title.text = getString(R.string.update_property)
        binding.saveButton.text = getString(R.string.save_change)
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

        viewModel.getCurrentFullProperty().observe(viewLifecycleOwner) { currentFullProperty ->

            if (currentFullProperty != null) {
                binding.priceEditText.setText(currentFullProperty.property.price.toString())
                binding.roomsEditText.setText(currentFullProperty.property.numberOfRooms.toString())
                binding.bedroomsEditText.setText(currentFullProperty.property.numberOfBedrooms.toString())
                binding.bathroomsEditText.setText(currentFullProperty.property.numberOfBathrooms.toString())
                binding.surfaceEditText.setText(currentFullProperty.property.surface.toString())
                binding.descriptionEditText.setText(currentFullProperty.property.description)
                binding.cityEditText.setText(currentFullProperty.property.address!!.city)
                binding.postcodeEditText.setText(currentFullProperty.property.address.postcode.toString())
                binding.streetEditText.setText(currentFullProperty.property.address.street)

                when (currentFullProperty.pointsOfInterestList[0].school) {
                    true -> binding.schoolChip.isChecked = true
                    false -> binding.schoolChip.isChecked = false
                }

                when (currentFullProperty.pointsOfInterestList[0].parks) {
                    true -> binding.parksChip.isChecked = true
                    false -> binding.parksChip.isChecked = false
                }

                when (currentFullProperty.pointsOfInterestList[0].shoppingCenter) {
                    true -> binding.shoppingCentreChip.isChecked = true
                    false -> binding.shoppingCentreChip.isChecked = false
                }

                when (currentFullProperty.pointsOfInterestList[0].sportsClubs) {
                    true -> binding.sportsClubsChip.isChecked = true
                    false -> binding.sportsClubsChip.isChecked = false
                }

                when (currentFullProperty.pointsOfInterestList[0].stations) {
                    true -> binding.stationsChip.isChecked = true
                    false -> binding.stationsChip.isChecked = false
                }

                when (currentFullProperty.pointsOfInterestList[0].university) {
                    true -> binding.universityChip.isChecked = true
                    false -> binding.universityChip.isChecked = false
                }

                val currentPropertyType = currentFullProperty.property.type
                val propertyTypeSpinner: AppCompatSpinner = binding.typeOfPropertySpinner
                val propertyTypeAdapter = ArrayAdapter.createFromResource(
                    requireContext(),
                    R.array.PropertiesTypes,
                    android.R.layout.simple_spinner_item
                )
                propertyTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                propertyTypeSpinner.adapter = propertyTypeAdapter
                propertyTypeSpinner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            p1: View?,
                            position: Int,
                            p3: Long,
                        ) {
                            val tSpinnerResult: String =
                                parent?.getItemAtPosition(position).toString()
                            resultPropertyTypeSpinner = tSpinnerResult
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {
                        }
                    }
                propertyTypeSpinner.setSelection(getCurrentPropertyTypeIndex(propertyTypeSpinner,
                    currentPropertyType))

                setUpRecyclerView(recyclerView, currentFullProperty.mediaList)
            }
        }

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

    private fun getCurrentPropertyTypeIndex(
        propertyTypeSpinner: AppCompatSpinner,
        s: String?,
    ): Int {
        for (i in 0..propertyTypeSpinner.count) {
            if (propertyTypeSpinner.getItemAtPosition(i).toString().contentEquals(s)) {
                return i
            }
        }
        return 0
    }

    private fun setUpRecyclerView(recyclerView: RecyclerView, mediaList: List<Media>) {
        val myLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = myLayoutManager
        recyclerView.adapter = UpdatePropertyAdapter(mediaList, this)
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

    override fun onDescriptionPhotoChanged(description: String, uri: String) {

    }

    override fun onDeleteMedia(media: Media) {

    }

}