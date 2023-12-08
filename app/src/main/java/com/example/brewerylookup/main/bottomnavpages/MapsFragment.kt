package com.example.brewerylookup.main.bottomnavpages

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.brewerylookup.R
import com.example.brewerylookup.databinding.FragmentMapsBinding
import com.example.inventory.common.BaseFragment
import com.example.inventory.common.SnackBarMessage
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
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
    private var isMapReady = false

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

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        checkLocationPermission()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        isMapReady = true // Set the flag to indicate that the map is ready

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)

        // Update the map to current location if permissions were already granted
        if (::lastLocation.isInitialized) {
            updateMapToCurrentLocation()
        }
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
                // We're supressing this since it's already "checkLocationPermission" method
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