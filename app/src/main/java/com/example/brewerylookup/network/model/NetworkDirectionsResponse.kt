package com.example.brewerylookup.network.model

data class NetworkDirectionsResponse(
    val geocodedWaypoints: List<NetworkGeocodedWaypoint>,
    val routes: List<NetworkRoute>,
    val status: String
)

data class NetworkGeocodedWaypoint(
    val geocoderStatus: String,
    val placeId: String,
    val types: List<String>
)

data class NetworkRoute(
    val bounds: NetworkBounds,
    val copyright: String,
    val legs: List<NetworkLeg>,
    val overview_polyline: NetworkPolyline,
    val summary: String,
    val warnings: List<String>,
    val waypointOrder: List<Int>
)

data class NetworkBounds(
    val northeast: NetworkLocation,
    val southwest: NetworkLocation
)

data class NetworkLocation(
    val lat: Double,
    val lng: Double
)

data class NetworkLeg(
    val distance: NetworkDistance,
    val duration: NetworkDuration,
    val end_address: String,
    val end_location: NetworkLocation,
    val start_address: String,
    val start_location: NetworkLocation,
    val steps: List<NetworkStep>,
    val trafficSpeedEntry: List<Any>,
    val viaWaypoint: List<Any>
)

data class NetworkDistance(
    val text: String,
    val value: Int
)

data class NetworkDuration(
    val text: String,
    val value: Int
)

data class NetworkStep(
    val distance: NetworkDistance,
    val duration: NetworkDuration,
    val endLocation: NetworkLocation,
    val htmlInstructions: String,
    val polyline: NetworkPolyline,
    val startLocation: NetworkLocation,
    val travel_mode: String,
    val maneuver: String?,
    val transitDetails: NetworkTransitDetails?,
    val travelRestriction: Boolean?
)

data class NetworkPolyline(
    val points: String
)

data class NetworkTransitDetails(
    val arrivalStop: NetworkTransitStop,
    val departureStop: NetworkTransitStop,
    val headsign: String,
    val line: NetworkTransitLine,
    val numStops: Int
)

data class NetworkTransitStop(
    val location: NetworkLocation,
    val name: String
)

data class NetworkTransitLine(
    val agencies: List<NetworkTransitAgency>,
    val color: String,
    val name: String,
    val shortName: String,
    val textColor: String,
    val url: String,
    val vehicle: NetworkVehicle
)

data class NetworkTransitAgency(
    val name: String,
    val phone: String,
    val url: String
)

data class NetworkVehicle(
    val icon: String,
    val localIcon: String,
    val name: String,
    val type: String
)