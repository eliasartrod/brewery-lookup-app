package com.example.brewerylookup.network

import com.example.brewerylookup.network.model.NetworkBreweryListResponse
import com.example.brewerylookup.network.model.NetworkDirectionsResponse
import com.example.brewerylookup.network.retrofit.BreweryLookupService
import com.example.brewerylookup.network.retrofit.GoogleMapsService

class BreweryLookupDataSource(
    private val breweryLookupService: BreweryLookupService,
    private val googleMapsService: GoogleMapsService
): NetworkDataSource {

    override suspend fun searchAllBreweries(
        pageNumber: Int,
        amountPerPage: Int?
    ): Result<List<NetworkBreweryListResponse>> {
        return breweryLookupService.searchAllBreweries(pageNumber, amountPerPage)
    }

    override suspend fun searchByFilter(
        breweryType: String?,
        state: String?,
        postalCode: String?,
        city: String?,
        breweryName: String?
    ): Result<List<NetworkBreweryListResponse>> {
        // Create a map to store non-null parameters
        val params = mutableMapOf<String, String>()

        // Add parameters to the map if they are not null or empty
        if (!breweryType.isNullOrEmpty()) {
            params["by_type"] = breweryType
        }
        if (!state.isNullOrEmpty()) {
            params["by_state"] = state
        }
        if (!postalCode.isNullOrEmpty()) {
            params["by_postal"] = postalCode
        }
        if (!city.isNullOrEmpty()) {
            params["by_city"] = city
        }
        if (!breweryName.isNullOrEmpty()) {
            params["by_name"] = breweryName
        }
        return breweryLookupService.searchByFilter(params)
    }

    override suspend fun getDirections(
        startingAddress: String,
        destinationAddress: String,
        apiKey: String
    ): Result<NetworkDirectionsResponse> {
        return googleMapsService.getDirections(destinationAddress, startingAddress, apiKey)
    }
}