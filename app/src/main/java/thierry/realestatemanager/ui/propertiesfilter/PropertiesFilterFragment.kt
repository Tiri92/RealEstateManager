package thierry.realestatemanager.ui.propertiesfilter

import android.os.Bundle
import android.view.*
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
        setHasOptionsMenu(true)

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
        val mediaTouchListener: Slider.OnSliderTouchListener = object :
            Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
            }

            override fun onStopTrackingTouch(slider: Slider) {
                viewModel.minMedia = slider.value.toInt()
            }
        }
        binding.mediaSlider.addOnSliderTouchListener(mediaTouchListener)
        binding.mediaSlider.setLabelFormatter { value: Float ->
            "$value media minimum"
        }

        //DATE PICKER CREATE
        binding.dateOfCreationPicker.setOnClickListener {
            if (datePicker == null || !datePicker!!.isAdded) {
                binding.dateOfCreationPicker.tag = "dateOfCreation"
                createDatePicker(it as TextView)
                datePicker!!.show(childFragmentManager.beginTransaction(), "DATE_PICKER")
            }
        }
        binding.cancelDateOfCreationFilter.setOnClickListener {
            if (viewModel.selectedDateOfCreation != null) {
                viewModel.selectedDateOfCreation = null
                binding.dateOfCreationPicker.text =
                    getString(R.string.property_put_up_for_sale_from_the_selected_date)
            }
        }

        //DATE PICKER SOLD
        binding.dateOfSalePicker.setOnClickListener {
            if (datePicker == null || !datePicker!!.isAdded) {
                binding.dateOfSalePicker.tag = "dateOfSale"
                createDatePicker(it as TextView)
                datePicker!!.show(childFragmentManager.beginTransaction(), "DATE_PICKER")
            }
        }
        binding.cancelDateOfSaleFilter.setOnClickListener {
            if (viewModel.selectedDateOfSale != null) {
                viewModel.selectedDateOfSale = null
                binding.dateOfSalePicker.text =
                    getString(R.string.property_sold_since_the_selected_date)
            }
        }

        val filterButton: AppCompatButton = binding.filterButton
        filterButton.setOnClickListener {

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

            if (viewModel.minMedia != null) {
                queryString += " INNER JOIN media_table ON media_table.propertyId = property_table.id"
            }

            queryString += " INNER JOIN points_of_interest_table ON points_of_interest_table.propertyId = property_table.id"

            if (viewModel.maxPrice != null && viewModel.minPrice != null) {
                containsCondition = true
                queryString += " Where property_table.price BETWEEN ${viewModel.minPrice} AND ${viewModel.maxPrice}"
            }

            if (viewModel.maxSurface != null && viewModel.minSurface != null) {
                if (containsCondition) {
                    queryString += " AND "
                } else {
                    queryString += " WHERE"
                    containsCondition = true
                }
                queryString += " property_table.surface BETWEEN ${viewModel.minSurface} AND ${viewModel.maxSurface}"
            }

            if (viewModel.selectedDateOfCreation != null) {
                if (containsCondition) {
                    queryString += " AND "
                } else {
                    queryString += " WHERE"
                    containsCondition = true
                }
                queryString += " property_table.dateOfCreation >= '${viewModel.selectedDateOfCreation}'"
            }

            if (viewModel.selectedDateOfSale != null) {
                queryString += if (containsCondition) {
                    " AND "
                } else {
                    " WHERE"
                }
                queryString += " property_table.dateOfSale >= '${viewModel.selectedDateOfSale}'"
            }

            if (viewModel.minMedia != null) {
                queryString += " GROUP BY media_table.propertyId,"
                queryString += " points_of_interest_table.propertyId HAVING COUNT(media_table.propertyId) >= ${viewModel.minMedia} AND points_of_interest_table.parks >= $parksState " +
                        "AND points_of_interest_table.school >= $schoolState AND points_of_interest_table.university >= $universityState AND points_of_interest_table.sportsClubs >= $sportsClubsState " +
                        "AND points_of_interest_table.stations >= $stationsState AND points_of_interest_table.shoppingCenter >= $shoppingCentreState"
            } else {
                queryString += " GROUP BY points_of_interest_table.propertyId HAVING points_of_interest_table.parks >= $parksState " +
                        "AND points_of_interest_table.school >= $schoolState AND points_of_interest_table.university >= $universityState AND points_of_interest_table.sportsClubs >= $sportsClubsState " +
                        "AND points_of_interest_table.stations >= $stationsState AND points_of_interest_table.shoppingCenter >= $shoppingCentreState"
            }

            queryString += ";"

            viewModel.getFilteredFullPropertyList(SimpleSQLiteQuery(queryString))
                .observe(viewLifecycleOwner) { fullPropertyList ->
                    viewModel.setFilteredFullPropertyList(fullPropertyList.toMutableList())
                    if (!fullPropertyList.isNullOrEmpty()) {
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

        }

        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.findItem(R.id.edit).isVisible = false
        menu.findItem(R.id.add).isVisible = false
        menu.findItem(R.id.filter).isVisible = false
        menu.findItem(R.id.map).isVisible = false
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
        datePicker!!.addOnPositiveButtonClickListener { selection ->
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
        }
    }

}