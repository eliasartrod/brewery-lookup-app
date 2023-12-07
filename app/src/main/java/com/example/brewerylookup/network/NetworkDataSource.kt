package com.example.brewerylookup.network

import com.example.brewerylookup.network.model.NetworkBreweryListResponse

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
}