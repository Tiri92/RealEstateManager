package thierry.realestatemanager.ui.propertydetail

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import thierry.realestatemanager.BuildConfig
import thierry.realestatemanager.R
import thierry.realestatemanager.databinding.FragmentPropertyDetailBinding
import thierry.realestatemanager.model.Media
import thierry.realestatemanager.utils.MediaUtils
import thierry.realestatemanager.utils.Utils
import java.time.format.DateTimeFormatter
import java.util.*

@AndroidEntryPoint
class PropertyDetailFragment : StaticMapRequestListener.Callback, Fragment() {

    private val viewModel: PropertyDetailViewModel by viewModels()
    private var currentPropertyId: String? = null
    private var _binding: FragmentPropertyDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var staticMap: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                currentPropertyId = it.getString(ARG_ITEM_ID)
                if (currentPropertyId != "") {
                    viewModel.setCurrentPropertyId(currentPropertyId.toString().toInt())
                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        when (currentPropertyId != "") {
            true -> {
                menu.findItem(R.id.edit).isVisible = true; menu.findItem(R.id.filter).isVisible =
                    false; menu.findItem(R.id.map).isVisible =
                    false; menu.findItem(R.id.add).isVisible = false
            }
            else -> menu.findItem(R.id.edit).isVisible = false
        }
        if (Utils.isTablet(requireContext())) {
            menu.findItem(R.id.filter).isVisible = true; menu.findItem(R.id.map).isVisible =
                true; menu.findItem(R.id.add).isVisible = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPropertyDetailBinding.inflate(inflater, container, false)
        val rootView = binding.root
        val recyclerView: RecyclerView = binding.recyclerviewFragmentDetail

        if (!currentPropertyId.isNullOrEmpty()) {
            viewModel.getCurrentFullProperty(currentPropertyId!!.toInt())
                .observe(viewLifecycleOwner) { currentFullProperty ->

                    if (currentFullProperty != null) {
                        binding.propertyDescription.text = currentFullProperty.property.description
                        binding.mediaTitle.text = getString(R.string.media)
                        binding.propertyDescriptionTitle.text = getString(R.string.description)
                        binding.propertySurface.text = getString(R.string.surface)
                        binding.propertySurfaceValue.text =
                            currentFullProperty.property.surface.toString()
                        binding.numberOfRooms.text = getString(R.string.number_rooms)
                        binding.numberOfRoomsValue.text =
                            currentFullProperty.property.numberOfRooms.toString()
                        binding.numberOfBathrooms.text = getString(R.string.number_bathrooms)
                        binding.numberOfBathroomsValue.text =
                            currentFullProperty.property.numberOfBathrooms.toString()
                        binding.numberOfBedrooms.text = getString(R.string.number_bedrooms)
                        binding.numberOfBedroomsValue.text =
                            currentFullProperty.property.numberOfBedrooms.toString()
                        binding.propertyAddress.text = getString(R.string.location)
                        val propertyAddress =
                            "${currentFullProperty.property.address?.street} \n ${currentFullProperty.property.address?.city} \n ${currentFullProperty.property.address?.postcode} \n " +
                                    "${currentFullProperty.property.address?.country}"
                        binding.propertyAddressValue.text = propertyAddress
                        if (Locale.getDefault().displayLanguage.equals("français")) {
                            val agentAndDateOfCreation =
                                "Créé par ${currentFullProperty.property.propertyAgent} le ${
                                    Utils.epochMilliToLocalDate(currentFullProperty.property.dateOfCreation)
                                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                }"
                            binding.agentAndDateOfCreationTextview.text = agentAndDateOfCreation
                        } else {
                            val agentAndDateOfCreation =
                                "Created by ${currentFullProperty.property.propertyAgent} on ${
                                    Utils.epochMilliToLocalDate(currentFullProperty.property.dateOfCreation)
                                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                }"
                            binding.agentAndDateOfCreationTextview.text = agentAndDateOfCreation
                        }

                        staticMap = binding.staticMap
                        val currentPropertyAddress: String =
                            currentFullProperty.property.address?.street!! + "+" + currentFullProperty.property.address.city + "+" + currentFullProperty.property.address.postcode + "+" + currentFullProperty.property.address.country
                        val staticMapUri =
                            "https://maps.googleapis.com/maps/api/staticmap?center=" + currentPropertyAddress + "&zoom=15&size=600x300&maptype=roadmap" + "&key=" + BuildConfig.MAPS_API_KEY

                        if (currentFullProperty.property.staticMapUri != null) {
                            Glide.with(this).load(currentFullProperty.property.staticMapUri)
                                .centerCrop()
                                .into(staticMap)
                        } else {
                            Glide.with(this).load(staticMapUri).centerCrop()
                                .listener(StaticMapRequestListener(this))
                                .into(staticMap)
                        }

                        when (currentFullProperty.pointsOfInterestList.school) {
                            true -> binding.schoolPoi.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_check_circle_24,
                                0,
                                0,
                                0)
                            false -> binding.schoolPoi.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_unpublished_24,
                                0,
                                0,
                                0)
                        }

                        when (currentFullProperty.pointsOfInterestList.university) {
                            true -> binding.universityPoi.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_check_circle_24,
                                0,
                                0,
                                0)
                            false -> binding.universityPoi.setCompoundDrawablesWithIntrinsicBounds(
                                R.drawable.baseline_unpublished_24,
                                0,
                                0,
                                0)
                        }

                        when (currentFullProperty.pointsOfInterestList.parks) {
                            true -> binding.parksPoi.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_check_circle_24,
                                0,
                                0,
                                0)
                            false -> binding.parksPoi.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_unpublished_24,
                                0,
                                0,
                                0)
                        }

                        if (!Utils.isTablet(requireContext())) {
                            when (currentFullProperty.pointsOfInterestList.sportsClubs) {
                                true -> binding.sportsClubsPoi.setCompoundDrawablesWithIntrinsicBounds(
                                    0,
                                    0,
                                    R.drawable.baseline_check_circle_24,
                                    0)
                                false -> binding.sportsClubsPoi.setCompoundDrawablesWithIntrinsicBounds(
                                    0,
                                    0,
                                    R.drawable.baseline_unpublished_24,
                                    0)
                            }

                            when (currentFullProperty.pointsOfInterestList.stations) {
                                true -> binding.stationsPoi.setCompoundDrawablesWithIntrinsicBounds(
                                    0,
                                    0,
                                    R.drawable.baseline_check_circle_24,
                                    0)
                                false -> binding.stationsPoi.setCompoundDrawablesWithIntrinsicBounds(
                                    0,
                                    0,
                                    R.drawable.baseline_unpublished_24,
                                    0)
                            }

                            when (currentFullProperty.pointsOfInterestList.shoppingCenter) {
                                true -> binding.shoppingCentrePoi.setCompoundDrawablesWithIntrinsicBounds(
                                    0,
                                    0,
                                    R.drawable.baseline_check_circle_24,
                                    0)
                                false -> binding.shoppingCentrePoi.setCompoundDrawablesWithIntrinsicBounds(
                                    0,
                                    0,
                                    R.drawable.baseline_unpublished_24,
                                    0)
                            }
                        } else {
                            when (currentFullProperty.pointsOfInterestList.sportsClubs) {
                                true -> binding.sportsClubsPoi.setCompoundDrawablesWithIntrinsicBounds(
                                    R.drawable.baseline_check_circle_24,
                                    0,
                                    0,
                                    0)
                                false -> binding.sportsClubsPoi.setCompoundDrawablesWithIntrinsicBounds(
                                    R.drawable.baseline_unpublished_24,
                                    0,
                                    0,
                                    0)
                            }

                            when (currentFullProperty.pointsOfInterestList.stations) {
                                true -> binding.stationsPoi.setCompoundDrawablesWithIntrinsicBounds(
                                    R.drawable.baseline_check_circle_24,
                                    0,
                                    0,
                                    0)
                                false -> binding.stationsPoi.setCompoundDrawablesWithIntrinsicBounds(
                                    R.drawable.baseline_unpublished_24,
                                    0,
                                    0,
                                    0)
                            }

                            when (currentFullProperty.pointsOfInterestList.shoppingCenter) {
                                true -> binding.shoppingCentrePoi.setCompoundDrawablesWithIntrinsicBounds(
                                    R.drawable.baseline_check_circle_24,
                                    0,
                                    0,
                                    0)
                                false -> binding.shoppingCentrePoi.setCompoundDrawablesWithIntrinsicBounds(
                                    R.drawable.baseline_unpublished_24,
                                    0,
                                    0,
                                    0)
                            }
                        }

                        viewModel.currentProperty = currentFullProperty.property
                        setUpRecyclerView(recyclerView,
                            currentFullProperty.mediaList.sortedBy { it.position },
                            childFragmentManager)
                    }
                }
        } else {
            binding.numberOfRooms.isVisible = false
            binding.numberOfBedrooms.isVisible = false
            binding.numberOfBathrooms.isVisible = false
            binding.propertySurface.isVisible = false
            binding.propertyAddress.isVisible = false
            binding.parksPoi.isVisible = false
            binding.universityPoi.isVisible = false
            binding.sportsClubsPoi.isVisible = false
            binding.schoolPoi.isVisible = false
            binding.stationsPoi.isVisible = false
            binding.shoppingCentrePoi.isVisible = false
        }

        return rootView
    }

    private fun setUpRecyclerView(
        recyclerView: RecyclerView,
        listOfPropertyMedia: List<Media>,
        supportFragmentManager: FragmentManager,
    ) {
        val myLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = myLayoutManager
        recyclerView.adapter = PropertyDetailAdapter(listOfPropertyMedia, supportFragmentManager)
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

    override fun onFailure(message: String?) {
    }

    override fun onSuccess(dataSource: Drawable) {
        val staticMapBitmap = dataSource.toBitmap()
        val fileName = "StaticMapPhoto"
        val fileDate: String = Utils.getTodayFormattedDateForMediaUri()
        val uriStaticMapPhoto: String =
            context?.filesDir.toString() + "/" + "$fileName$fileDate.jpg"
        if (MediaUtils.savePhotoToInternalMemory(
                fileName,
                fileDate,
                staticMapBitmap,
                requireContext()
            )
        ) {
            viewModel.currentProperty.staticMapUri = uriStaticMapPhoto
            viewModel.updateProperty(viewModel.currentProperty)
        }
    }

}