package thierry.realestatemanager.ui.propertiesfilter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.sqlite.db.SimpleSQLiteQuery
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider
import dagger.hilt.android.AndroidEntryPoint
import thierry.realestatemanager.databinding.FragmentPropertiesFilterBinding
import java.text.NumberFormat
import java.util.*
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener

import com.google.android.material.datepicker.MaterialDatePicker
import thierry.realestatemanager.R
import thierry.realestatemanager.utils.Utils
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class PropertiesFilterFragment : Fragment() {

    private val viewModel: PropertiesFilterViewModel by viewModels()
    private var _binding: FragmentPropertiesFilterBinding? = null
    private val binding get() = _binding!!
    private var minPrice: Int? = null
    private var maxPrice: Int? = null
    private var minSurface: Int? = null
    private var maxSurface: Int? = null
    private var minMedia: Int? = null
    private var datePicker: MaterialDatePicker<Long>? = null
    private var currentDate: LocalDate? = null
    private var currentFormattedDate: String? = null
    private var currentLongDate: Long? = null
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
                minPrice = slider.values[0].toInt()
                maxPrice = slider.values[1].toInt()
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
                minSurface = slider.values[0].toInt()
                maxSurface = slider.values[1].toInt()
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
                minMedia = slider.value.toInt()
            }
        }
        binding.photoSlider.addOnSliderTouchListener(photoTouchListener)
        binding.photoSlider.setLabelFormatter { value: Float ->
            "$value media minimum"
        }

        //DATE PICKER
        binding.textviewDatePicker.setOnClickListener(View.OnClickListener {
            if (datePicker == null || !datePicker!!.isAdded) {
                createDatePicker()
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
            var containsOtherCondition = false

            queryString += "SELECT * FROM property_table"

            if (maxPrice != null && minPrice != null) {
                containsCondition = true
                queryString += " Where price BETWEEN $minPrice AND $maxPrice"
            }

            if (maxSurface != null && minSurface != null) {
                if (containsCondition) {
                    queryString += " AND "
                } else {
                    queryString += " WHERE"
                    containsCondition = true
                }
                queryString += " surface BETWEEN $minSurface AND $maxSurface"
            }

            if (currentFormattedDate != null) {
                if (containsCondition) {
                    queryString += " AND "
                } else {
                    queryString += " WHERE"
                    containsCondition = true
                }
                queryString += " dateOfCreation >= '$currentLongDate'"
            }

            if (minMedia != null) {
                queryString += " INNER JOIN media_table ON propertyId = property_table.id GROUP BY media_table.propertyId HAVING COUNT(media_table.propertyId) >= $minMedia"
                containsOtherCondition = true
            }

//            queryString += " INNER JOIN points_of_interest_table ON propertyId = property_table.id GROUP BY points_of_interest_table.propertyId HAVING points_of_interest_table.parks >= $parksState " +
//                    "AND points_of_interest_table.school >= $schoolState AND points_of_interest_table.university >= $universityState AND points_of_interest_table.sportsClubs >= $sportsClubsState " +
//                    "AND points_of_interest_table.stations >= $stationsState AND points_of_interest_table.shoppingCenter >= $shoppingCentreState"

            queryString += ";"

            viewModel.getFilteredFullPropertyList(SimpleSQLiteQuery(queryString))
                .observe(viewLifecycleOwner) {
                    it?.forEach {
                        Log.i("THIERRYBITAR", "${it.property.type}")
                    }
                }

            val navHostFragment =
                requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment_property_detail) as NavHostFragment
            navController = navHostFragment.navController
            navController.navigateUp()

        })

        return rootView
    }

    private fun createDatePicker() {
        datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select a date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()
        datePicker!!.addOnPositiveButtonClickListener(MaterialPickerOnPositiveButtonClickListener { selection ->
            currentLongDate = selection
            currentDate = Utils.epochMilliToLocalDate(selection)
            currentFormattedDate = currentDate!!.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            binding.textviewDatePicker.text = currentFormattedDate
        })
    }

}