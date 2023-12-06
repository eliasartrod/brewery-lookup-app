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
}