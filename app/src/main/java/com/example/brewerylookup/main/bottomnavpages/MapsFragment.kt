package com.example.brewerylookup.main.bottomnavpages

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.brewerylookup.R
import com.example.brewerylookup.databinding.FragmentMapsBinding
import com.example.brewerylookup.main.MainViewModel
import com.example.brewerylookup.model.Directions
import com.example.brewerylookup.model.Route
import com.example.inventory.common.BaseFragment
import com.example.inventory.common.SnackBarMessage
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.PolyUtil
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsFragment : BaseFragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private lateinit var binding: FragmentMapsBinding
    private lateinit var mMap: GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var textWatcher: TextWatcher
    private var isMapReady = false

    private val viewModel: MainViewModel by viewModels()

    enum class Status {
        NOT_FOUND,
        ZERO_RESULTS
    }

    enum class MarkerType {
        START, END
    }

    override fun getRoot(): View? {
        return binding.root
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActionBarTitle(getString(R.string.brewery_map_it))

        viewModel.setApiKey()
        viewModel.directionsResult.observe(viewLifecycleOwner) { updateMapWithDirections(it) }

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        checkLocationPermission()
        setupWatcher()
    }

    private fun setupWatcher() {
        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                updateApplyButtonState()
                binding.actionApply.setOnClickListener {
                    searchDirections(
                        binding.editTextStartingAddress.text.toString(),
                        binding.editTextDestination.text.toString()
                    )
                }
            }
        }
        binding.editTextStartingAddress.addTextChangedListener(textWatcher)
        binding.editTextDestination.addTextChangedListener(textWatcher)
    }

    private fun updateApplyButtonState() {
        binding.actionApply.isEnabled =
            (binding.editTextStartingAddress.text.toString().isNotEmpty()
                    && (binding.editTextDestination.text.toString().isNotEmpty()))
    }

    private fun searchDirections(startingAddress: String, destinationAddress: String) {
        viewModel.searchDirections(startingAddress, destinationAddress)
    }

    private fun updateMapWithDirections(directions: Directions?) {
        if (directions != null) {
            if (directions.status == Status.NOT_FOUND.toString() || directions.status == Status.ZERO_RESULTS.toString()) {
                showSnackBar(SnackBarMessage(R.string.error_message_no_route_found))
                updateMapToCurrentLocation()
            }
        }
        mMap.clear()

        // Check if the directions object and routes are not null
        if (directions != null && !directions.routes.isNullOrEmpty()) {
            val polylinePoints = directions.routes[0].overviewPolyline?.points

            // Draw the polyline on the map if polyline points are available
            if (polylinePoints != null) {
                drawPolyline(polylinePoints)

                // Zoom to fit the entire route
                fitRouteOnMap(directions.routes[0])
            }
        }
    }

    private fun drawPolyline(polylinePoints: String) {
        val decodedPoints = PolyUtil.decode(polylinePoints)

        val polylineOptions = PolylineOptions().apply {
            addAll(decodedPoints)
            color(Color.BLUE)
            width(5f)
        }
        mMap.addPolyline(polylineOptions)
    }

    private fun placeMarkerOnMap(currentLatLong: LatLng, markerType: MarkerType, duration: String? = null) {
        val markerOptions = MarkerOptions().position(currentLatLong)

        when (markerType) {
            MarkerType.START -> {
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                markerOptions.title("Start")
            }
            MarkerType.END -> {
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                markerOptions.title("End")
            }
        }

        duration?.let {
            markerOptions.snippet("Duration: $it")
        }

        mMap.addMarker(markerOptions)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        isMapReady = true // Set the flag to indicate that the map is ready

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)

        // Update the map to the current location if permissions were already granted
        if (::lastLocation.isInitialized) {
            updateMapToCurrentLocation()
        }
    }

    private fun fitRouteOnMap(route: Route) {
        val boundsBuilder = LatLngBounds.Builder()

        route.legs?.forEach { leg ->
            if (leg.startLocation?.lat != null && leg.startLocation.lng != null) {
                placeMarkerOnMap(
                    LatLng(leg.startLocation.lat, leg.startLocation.lng),
                    MarkerType.START,
                    leg.duration?.text
                )
                boundsBuilder.include(LatLng(leg.startLocation.lat, leg.startLocation.lng))
            }

            if (leg.endLocation?.lat != null && leg.endLocation.lng != null) {
                placeMarkerOnMap(
                    LatLng(leg.endLocation.lat, leg.endLocation.lng),
                    MarkerType.END,
                    leg.duration?.text
                )
                boundsBuilder.include(LatLng(leg.endLocation.lat, leg.endLocation.lng))
            }
        }

        val bounds = boundsBuilder.build()

        // Check if the bounds have valid points before animating the camera
        val padding = 100 // Adjust the padding as needed
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
        mMap.animateCamera(cameraUpdate)
    }

    private fun placeMarkerOnMap(currentLatLong: LatLng) {
        val markerOptions = MarkerOptions().position(currentLatLong)
        markerOptions.title("$currentLatLong")
        mMap.addMarker(markerOptions)
    }

    private fun checkLocationPermission() {
        Dexter.withActivity(activity)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                // We're suppressing this since it's already "checkLocationPermission" method
                @SuppressLint("MissingPermission")
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    // Enable My Location if the map is ready
                    if (isMapReady) {
                        mMap.isMyLocationEnabled = true
                    }

                    // Fetch the last known location and update the map
                    fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->
                        if (location != null) {
                            lastLocation = location
                            updateMapToCurrentLocation()
                        }
                    }
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    // Handle permission denial
                    showSnackBar(SnackBarMessage(R.string.error_message_map_permission))
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    // Show a rationale for the permission if needed
                    // This is called if the user denied the permission previously
                    token?.continuePermissionRequest()
                }
            }).check()
    }

    private fun updateMapToCurrentLocation() {
        if (isMapReady) {
            val currentLatLong = LatLng(lastLocation.latitude, lastLocation.longitude)
            placeMarkerOnMap(currentLatLong)
            mMap.animateCamera(CameraUpdateFactory.newLatLng(currentLatLong))
        }
    }

    override fun onMarkerClick(p0: Marker) = false
}