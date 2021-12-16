package thierry.realestatemanager.ui.propertiesfilter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.sqlite.db.SimpleSQLiteQuery
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import thierry.realestatemanager.R
import thierry.realestatemanager.databinding.FragmentPropertiesFilterBinding
import thierry.realestatemanager.utils.Utils
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.*

@AndroidEntryPoint
class PropertiesFilterFragment : Fragment() {

    private val viewModel: PropertiesFilterViewModel by viewModels()
    private var _binding: FragmentPropertiesFilterBinding? = null
    private val binding get() = _binding!!
    private var datePicker: MaterialDatePicker<Long>? = null
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPropertiesFilterBinding.inflate(inflater, container, false)
        val rootView = binding.root

        //PRICE
        val priceTouchListener: RangeSlider.OnSliderTouchListener = object :
            RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {
            }

            override fun onStopTrackingTouch(slider: RangeSlider) {
                viewModel.minPrice = slider.values[0].toInt()
                viewModel.maxPrice = slider.values[1].toInt()
            }
        }
        binding.priceSlider.addOnSliderTouchListener(priceTouchListener)
        binding.priceSlider.setLabelFormatter { value: Float ->
            val format = NumberFormat.getCurrencyInstance()
            format.maximumFractionDigits = 0
            format.currency = Currency.getInstance("USD")
            format.format(value.toDouble())
        }

        //SURFACE
        val surfaceTouchListener: RangeSlider.OnSliderTouchListener = object :
            RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {
            }

            override fun onStopTrackingTouch(slider: RangeSlider) {
                viewModel.minSurface = slider.values[0].toInt()
                viewModel.maxSurface = slider.values[1].toInt()
            }
        }
        binding.surfaceSlider.addOnSliderTouchListener(surfaceTouchListener)
        binding.surfaceSlider.setLabelFormatter { value: Float ->
            value.toString() + "m²"
        }

        //MEDIA
        val photoTouchListener: Slider.OnSliderTouchListener = object :
            Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
            }

            override fun onStopTrackingTouch(slider: Slider) {
                viewModel.minMedia = slider.value.toInt()
            }
        }
        binding.photoSlider.addOnSliderTouchListener(photoTouchListener)
        binding.photoSlider.setLabelFormatter { value: Float ->
            "$value media minimum"
        }

        //DATE PICKER
        binding.dateOfCreationPicker.setOnClickListener(View.OnClickListener {
            if (datePicker == null || !datePicker!!.isAdded) {
                binding.dateOfCreationPicker.tag = "dateOfCreation"
                createDatePicker(it as TextView)
                datePicker!!.show(childFragmentManager.beginTransaction(), "DATE_PICKER")
            }
        })

        //Date PICKER2
        binding.dateOfSalePicker.setOnClickListener(View.OnClickListener {
            if (datePicker == null || !datePicker!!.isAdded) {
                binding.dateOfSalePicker.tag = "dateOfSale"
                createDatePicker(it as TextView)
                datePicker!!.show(childFragmentManager.beginTransaction(), "DATE_PICKER")
            }
        })

        val filterButton: AppCompatButton = binding.filterButton
        filterButton.setOnClickListener(View.OnClickListener {

            var schoolState = false
            var universityState = false
            var parksState = false
            var sportsClubsState = false
            var stationsState = false
            var shoppingCentreState = false
            val pointsOfInterestChipGroup: ChipGroup = binding.pointsOfInterestChipGroup
            pointsOfInterestChipGroup.checkedChipIds.forEach { chipItem ->
                val chipText =
                    binding.pointsOfInterestChipGroup.findViewById<Chip>(chipItem).text.toString()
                val chipState =
                    binding.pointsOfInterestChipGroup.findViewById<Chip>(chipItem).isChecked
                when (chipText) {
                    "School" -> schoolState = chipState
                    "University" -> universityState = chipState
                    "Parks" -> parksState = chipState
                    "Sports clubs" -> sportsClubsState = chipState
                    "Stations" -> stationsState = chipState
                    "Shopping centre" -> shoppingCentreState = chipState
                }
            }

            var queryString = ""
            var containsCondition = false

            queryString += "SELECT * FROM property_table"

            if (viewModel.maxPrice != null && viewModel.minPrice != null) {
                containsCondition = true
                queryString += " Where price BETWEEN ${viewModel.minPrice} AND ${viewModel.maxPrice}"
            }

            if (viewModel.maxSurface != null && viewModel.minSurface != null) {
                if (containsCondition) {
                    queryString += " AND "
                } else {
                    queryString += " WHERE"
                    containsCondition = true
                }
                queryString += " surface BETWEEN ${viewModel.minSurface} AND ${viewModel.maxSurface}"
            }

            if (viewModel.selectedDateOfCreation != null) {
                if (containsCondition) {
                    queryString += " AND "
                } else {
                    queryString += " WHERE"
                    containsCondition = true
                }
                queryString += " dateOfCreation >= '${viewModel.selectedDateOfCreation}'"
            }

            if (viewModel.selectedDateOfSale != null) {
                if (containsCondition) {
                    queryString += " AND "
                } else {
                    queryString += " WHERE"
                }
                queryString += " dateOfSale >= '${viewModel.selectedDateOfSale}'"
            }

            if (viewModel.minMedia != null) {
                queryString += " INNER JOIN media_table ON propertyId = property_table.id GROUP BY media_table.propertyId HAVING COUNT(media_table.propertyId) >= ${viewModel.minMedia}"
            }

//            queryString += " INNER JOIN points_of_interest_table ON propertyId = property_table.id GROUP BY points_of_interest_table.propertyId HAVING points_of_interest_table.parks >= $parksState " +
//                    "AND points_of_interest_table.school >= $schoolState AND points_of_interest_table.university >= $universityState AND points_of_interest_table.sportsClubs >= $sportsClubsState " +
//                    "AND points_of_interest_table.stations >= $stationsState AND points_of_interest_table.shoppingCenter >= $shoppingCentreState"

            queryString += ";"

            viewModel.getFilteredFullPropertyList(SimpleSQLiteQuery(queryString))
                .observe(viewLifecycleOwner) {
                    viewModel.setFilteredFullPropertyList(it.toMutableList())
                    it?.forEach {
                        Log.i("THIERRYBITAR", "${it.property.type}")
                    }
                    if (!it.isNullOrEmpty()) {
                        val navHostFragment =
                            requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment_property_detail) as NavHostFragment
                        navController = navHostFragment.navController
                        navController.navigateUp()
                    } else {
                        Snackbar.make(requireView(),
                            "No results, change filters",
                            Snackbar.LENGTH_SHORT).show()
                    }
                }

        })

        return rootView
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.selectedDateOfCreation != null) {
            binding.dateOfCreationPicker.text = viewModel.selectedFormattedDateOfCreation
        }
        if (viewModel.selectedDateOfSale != null) {
            binding.dateOfSalePicker.text = viewModel.selectedFormattedDateOfSale
        }
    }

    private fun createDatePicker(view: TextView) {
        datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select a date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()
        datePicker!!.addOnPositiveButtonClickListener(MaterialPickerOnPositiveButtonClickListener { selection ->
            if (view.tag == "dateOfCreation") {
                viewModel.selectedDateOfCreation = selection
                viewModel.selectedFormattedDateOfCreation = Utils.epochMilliToLocalDate(selection)
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                view.text = viewModel.selectedFormattedDateOfCreation
            } else {
                viewModel.selectedDateOfSale = selection
                viewModel.selectedFormattedDateOfSale = Utils.epochMilliToLocalDate(selection)
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                view.text = viewModel.selectedFormattedDateOfSale
            }
        })
    }

}