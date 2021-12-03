package thierry.realestatemanager.ui.googlemap

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import thierry.realestatemanager.BuildConfig
import thierry.realestatemanager.R
import thierry.realestatemanager.databinding.FragmentGoogleMapBinding
import thierry.realestatemanager.databinding.FragmentPropertyDetailBinding
import thierry.realestatemanager.ui.addproperty.AddPropertyViewModel

@AndroidEntryPoint
class GoogleMapFragment : Fragment() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var _binding: FragmentGoogleMapBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GoogleMapViewModel by viewModels()
    private lateinit var map: GoogleMap

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->

        map = googleMap
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.isMyLocationEnabled = true
    }

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoogleMapBinding.inflate(inflater, container, false)
        val rootView = binding.root
        setHasOptionsMenu(true)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    viewModel.currentPosition = viewModel.setLocationInLatLng(location)
                    moveAndDisplayMyPosition(viewModel.currentPosition!!)
                }
            }

        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    // Precise location access granted.
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    // Only approximate location access granted.
                }
                else -> {
                    // No location access granted.
                }
            }
        }

        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun moveAndDisplayMyPosition(location: LatLng) {
        map.clear()
        map.addMarker(
            MarkerOptions()
                .position(location)
                .title("My position")
        )
        val cameraPosition =
            CameraPosition.Builder().target(location)
                .zoom(14f).tilt(30f).bearing(0f).build()
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.findItem(R.id.map).isVisible = false
        menu.findItem(R.id.add).isVisible = false
        menu.findItem(R.id.edit).isVisible = false
    }

}