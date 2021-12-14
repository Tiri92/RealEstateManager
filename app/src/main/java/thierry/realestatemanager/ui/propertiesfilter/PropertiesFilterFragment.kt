package thierry.realestatemanager.ui.propertiesfilter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.sqlite.db.SimpleSQLiteQuery
import com.google.android.material.slider.RangeSlider
import dagger.hilt.android.AndroidEntryPoint
import thierry.realestatemanager.databinding.FragmentPropertiesFilterBinding
import java.text.NumberFormat
import java.util.*


@AndroidEntryPoint
class PropertiesFilterFragment : Fragment() {

    private val viewModel: PropertiesFilterViewModel by viewModels()
    private var _binding: FragmentPropertiesFilterBinding? = null
    private val binding get() = _binding!!
    private var minPrice: Int? = null
    private var maxPrice: Int? = null
    private var minSurface: Int? = null
    private var maxSurface: Int? = null

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

        val filterButton: AppCompatButton = binding.filterButton
        filterButton.setOnClickListener(View.OnClickListener {

            var queryString = ""
            var containsCondition = false

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

            queryString += ";"

            viewModel.getFilteredFullPropertyList(SimpleSQLiteQuery(queryString))
                .observe(viewLifecycleOwner) {
                    it?.forEach {
                        Log.i("THIERRYBITAR", "${it.property.type}")
                    }
                }

        })

        return rootView
    }

}