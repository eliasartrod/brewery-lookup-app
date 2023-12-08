package com.example.brewerylookup.model

import com.example.brewerylookup.network.model.*

data class Directions(
    val geocodedWaypoints: List<GeocodedWaypoint>?,
    val routes: List<Route>?,
    val status: String?
) {
    companion object {
        fun fromNetwork(networkResponse: NetworkDirectionsResponse): Directions {
            return Directions(
                geocodedWaypoints = networkResponse.geocodedWaypoints?.map { GeocodedWaypoint.fromNetwork(it) },
                routes = networkResponse.routes?.map { Route.fromNetwork(it) },
                status = networkResponse.status
            )
        }
    }
}

data class GeocodedWaypoint(
    val geocoderStatus: String?,
    val placeId: String?,
    val types: List<String>?
) {
    companion object {
        fun fromNetwork(networkResponse: NetworkGeocodedWaypoint): GeocodedWaypoint {
            return GeocodedWaypoint(
                geocoderStatus = networkResponse.geocoderStatus,
                placeId = networkResponse.placeId,
                types = networkResponse.types
            )
        }
    }
}

data class Route(
    val bounds: Bounds?,
    val copyright: String?,
    val legs: List<Leg>?,
    val overviewPolyline: Polyline?,
    val summary: String?,
    val warnings: List<String>?,
    val waypointOrder: List<Int>?
) {
    companion object {
        fun fromNetwork(networkResponse: NetworkRoute): Route {
            return Route(
                bounds = Bounds.fromNetwork(networkResponse.bounds),
                copyright = networkResponse.copyright,
                legs = networkResponse.legs?.map { Leg.fromNetwork(it) },
                overviewPolyline = Polyline.fromNetwork(networkResponse.overview_polyline),
                summary = networkResponse.summary,
                warnings = networkResponse.warnings,
                waypointOrder = networkResponse.waypointOrder
            )
        }
    }
}

data class Bounds(
    val northeast: Location?,
    val southwest: Location?
) {
    companion object {
        fun fromNetwork(networkResponse: NetworkBounds): Bounds {
            return Bounds(
                northeast = Location.fromNetwork(networkResponse.northeast),
                southwest = Location.fromNetwork(networkResponse.southwest)
            )
        }
    }
}

data class Location(
    val lat: Double?,
    val lng: Double?
) {
    companion object {
        fun fromNetwork(networkResponse: NetworkLocation?): Location? {
            return networkResponse?.let {
                Location(
                    lat = it.lat,
                    lng = it.lng
                )
            }
        }
    }
}

data class Leg(
    val distance: Distance?,
    val duration: Duration?,
    val endAddress: String?,
    val endLocation: Location?,
    val startAddress: String?,
    val startLocation: Location?,
    val steps: List<Step>?,
    val trafficSpeedEntry: List<Any>?,
    val viaWaypoint: List<Any>?
) {
    companion object {
        fun fromNetwork(networkResponse: NetworkLeg): Leg {
            return Leg(
                distance = Distance.fromNetwork(networkResponse.distance),
                duration = Duration.fromNetwork(networkResponse.duration),
                endAddress = networkResponse.end_address,
                endLocation = Location.fromNetwork(networkResponse.end_location),
                startAddress = networkResponse.start_address,
                startLocation = Location.fromNetwork(networkResponse.start_location),
                steps = networkResponse.steps?.map { Step.fromNetwork(it) },
                trafficSpeedEntry = networkResponse.trafficSpeedEntry,
                viaWaypoint = networkResponse.viaWaypoint
            )
        }
    }
}

data class Distance(
    val text: String?,
    val value: Int?
) {
    companion object {
        fun fromNetwork(networkResponse: NetworkDistance): Distance {
            return Distance(
                text = networkResponse.text,
                value = networkResponse.value
            )
        }
    }
}

data class Duration(
    val text: String?,
    val value: Int?
) {
    companion object {
        fun fromNetwork(networkResponse: NetworkDuration): Duration {
            return Duration(
                text = networkResponse.text,
                value = networkResponse.value
            )
        }
    }
}

data class Step(
    val distance: Distance?,
    val duration: Duration?,
    val endLocation: Location?,
    val htmlInstructions: String?,
    val polyline: Polyline?,
    val startLocation: Location?,
    val travelMode: String?,
    val maneuver: String?,
    val transitDetails: TransitDetails?,
    val travelRestriction: Boolean?
) {
    companion object {
        fun fromNetwork(networkResponse: NetworkStep): Step {
            return Step(
                distance = Distance.fromNetwork(networkResponse.distance),
                duration = Duration.fromNetwork(networkResponse.duration),
                endLocation = Location.fromNetwork(networkResponse.endLocation),
                htmlInstructions = networkResponse.htmlInstructions,
                polyline = Polyline.fromNetwork(networkResponse.polyline),
                startLocation = Location.fromNetwork(networkResponse.startLocation),
                travelMode = networkResponse.travel_mode,
                maneuver = networkResponse.maneuver,
                transitDetails = networkResponse.transitDetails?.let { TransitDetails.fromNetwork(it) },
                travelRestriction = networkResponse.travelRestriction
            )
        }
    }
}

data class Polyline(
    val points: String?
) {
    companion object {
        fun fromNetwork(networkResponse: NetworkPolyline?): Polyline? {
            return networkResponse?.let {
                Polyline(
                    points = it.points
                )
            }
        }
    }
}

data class TransitDetails(
    val arrivalStop: TransitStop?,
    val departureStop: TransitStop?,
    val headsign: String?,
    val line: TransitLine?,
    val numStops: Int?
) {
    companion object {
        fun fromNetwork(networkResponse: NetworkTransitDetails): TransitDetails {
            return TransitDetails(
                arrivalStop = networkResponse.arrivalStop?.let { TransitStop.fromNetwork(it) },
                departureStop = networkResponse.departureStop?.let { TransitStop.fromNetwork(it) },
                headsign = networkResponse.headsign,
                line = networkResponse.line?.let { TransitLine.fromNetwork(it) },
                numStops = networkResponse.numStops
            )
        }
    }
}

data class TransitStop(
    val location: Location?,
    val name: String?
) {
    companion object {
        fun fromNetwork(networkResponse: NetworkTransitStop): TransitStop {
            return TransitStop(
                location = networkResponse.location?.let { Location.fromNetwork(it) },
                name = networkResponse.name
            )
        }
    }
}

data class TransitLine(
    val agencies: List<TransitAgency>?,
    val color: String?,
    val name: String?,
    val shortName: String?,
    val textColor: String?,
    val url: String?,
    val vehicle: Vehicle?
) {
    companion object {
        fun fromNetwork(networkResponse: NetworkTransitLine): TransitLine {
            return TransitLine(
                agencies = networkResponse.agencies?.map { TransitAgency.fromNetwork(it) },
                color = networkResponse.color,
                name = networkResponse.name,
                shortName = networkResponse.shortName,
                textColor = networkResponse.textColor,
                url = networkResponse.url,
                vehicle = networkResponse.vehicle?.let { Vehicle.fromNetwork(it) }
            )
        }
    }
}

data class TransitAgency(
    val name: String?,
    val phone: String?,
    val url: String?
) {
    companion object {
        fun fromNetwork(networkResponse: NetworkTransitAgency): TransitAgency {
            return TransitAgency(
                name = networkResponse.name,
                phone = networkResponse.phone,
                url = networkResponse.url
            )
        }
    }
}

data class Vehicle(
    val icon: String?,
    val localIcon: String?,
    val name: String?,
    val type: String?
) {
    companion object {
        fun fromNetwork(networkResponse: NetworkVehicle): Vehicle {
            return Vehicle(
                icon = networkResponse.icon,
                localIcon = networkResponse.localIcon,
                name = networkResponse.name,
                type = networkResponse.type
            )
        }
    }
}