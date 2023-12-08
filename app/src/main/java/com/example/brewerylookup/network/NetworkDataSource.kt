package com.example.brewerylookup.network

import com.example.brewerylookup.network.model.NetworkBreweryListResponse
import com.example.brewerylookup.network.model.NetworkDirectionsResponse

/**
 * Definition of network access data methods
 */
interface NetworkDataSource {

    suspend fun searchAllBreweries(
        pageNumber: Int,
        amountPerPage: Int?
    ): Result<List<NetworkBreweryListResponse>>

    suspend fun searchByFilter(
        breweryType: String?,
        state: String?,
        postalCode: String?,
        city: String?,
        breweryName: String?
    ): Result<List<NetworkBreweryListResponse>>

    suspend fun getDirections(
        startingAddress: String,
        destinationAddress: String,
        apiKey: String
    ): Result<NetworkDirectionsResponse>
}