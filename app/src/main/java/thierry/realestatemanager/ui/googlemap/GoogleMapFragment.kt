package thierry.realestatemanager.ui.googlemap

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import thierry.realestatemanager.R
import thierry.realestatemanager.databinding.FragmentGoogleMapBinding
import thierry.realestatemanager.ui.propertydetail.PropertyDetailFragment
import thierry.realestatemanager.utils.UiUtils

private const val LOCATION_REQUEST_INTERVAL_MS = 10000
private const val SMALLEST_DISPLACEMENT_THRESHOLD_METER = 25f

@AndroidEntryPoint
class GoogleMapFragment : Fragment() {

    private val viewModel: GoogleMapViewModel by viewModels()
    private var _binding: FragmentGoogleMapBinding? = null
    private val binding get() = _binding!!
    private var map: GoogleMap? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        val paris = LatLng(-48.86, 2.34)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(paris))
        googleMap.uiSettings.isZoomControlsEnabled = true
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
        googleMap.setOnMarkerClickListener { marker ->
            val propertyId = marker.tag
            val bundle = Bundle()
            bundle.putString(
                PropertyDetailFragment.ARG_ITEM_ID,
                propertyId.toString()
            )
            findNavController().navigate(R.id.action_GoogleMapFragment_to_property_detail_fragment,
                bundle)
            false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentGoogleMapBinding.inflate(inflater, container, false)
        val rootView = binding.root
        setHasOptionsMenu(true)

        viewModel.getFullPropertyList().observe(viewLifecycleOwner) { fullPropertyList ->
            for (fullProperty in fullPropertyList) {
                if (fullProperty.property.address!!.propertyLatitude != null && fullProperty.property.address.propertyLongitude != null) {
                    if (map != null) {
                        UiUtils.addMarker(map!!,
                            requireContext(),
                            fullProperty.property.address.propertyLatitude!!,
                            fullProperty.property.address.propertyLongitude!!,
                            fullProperty.property.type.toString(), fullProperty.property.id)
                    }
                } else {
                    viewModel.currentProperty = fullProperty.property
                    val address =
                        "${fullProperty.property.address.street}%20${fullProperty.property.address.city}%20${fullProperty.property.address.postcode}"
                    viewModel.callGeocoding(address, fullProperty.property.id)
                    break
                }
            }
        }

        viewModel.getGeocodingResponse()
            .observe(viewLifecycleOwner) { geocodingResponse ->
                if (map != null && !geocodingResponse.results.isNullOrEmpty()) {
                    viewModel.currentProperty.address!!.propertyLatitude =
                        geocodingResponse.results[0]!!.geometry!!.location!!.lat!!
                    viewModel.currentProperty.address!!.propertyLongitude =
                        geocodingResponse.results[0]!!.geometry!!.location!!.lng!!
                    viewModel.currentProperty.id = geocodingResponse.propertyId!!
                    if (viewModel.currentProperty.address!!.propertyLatitude != null && viewModel.currentProperty.address!!.propertyLongitude != null) {
                        viewModel.updateProperty(viewModel.currentProperty)
                    }
                }
            }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        if (requestCode == 0 && grantResults[0] != -1) {
            UiUtils.checkGpsState(binding.root, requireActivity())
            launchGeolocationRequest()
            map!!.isMyLocationEnabled = true
        } else {
            Log.i("", "")
        }
    }

    @SuppressLint("MissingPermission")
    fun launchGeolocationRequest() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient!!.removeLocationUpdates(locationCallback)
        fusedLocationClient!!.requestLocationUpdates(
            LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setSmallestDisplacement(SMALLEST_DISPLACEMENT_THRESHOLD_METER)
                .setInterval(LOCATION_REQUEST_INTERVAL_MS.toLong()),
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            viewModel.currentPosition = viewModel.setLocationInLatLng(locationResult.lastLocation)
            moveAndDisplayUserPosition(viewModel.currentPosition!!)
        }
    }

    private fun moveAndDisplayUserPosition(location: LatLng) {
        val cameraPosition =
            CameraPosition.Builder().target(location)
                .zoom(14f).tilt(30f).bearing(0f).build()
        map!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.findItem(R.id.map).isVisible = false
        menu.findItem(R.id.add).isVisible = false
        menu.findItem(R.id.edit).isVisible = false
    }

}