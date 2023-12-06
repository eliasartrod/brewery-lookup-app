package com.example.brewerylookup.network

import com.example.brewerylookup.network.model.NetworkBreweryListResponse
import com.example.brewerylookup.network.retrofit.BreweryLookupService

class BreweryLookupDataSource(
    private val breweryLookupService: BreweryLookupService
): NetworkDataSource {

    override suspend fun searchAllBreweries(
        amountPerPage: Int?
    ): Result<List<NetworkBreweryListResponse>> {
        return breweryLookupService.searchAllBreweries(amountPerPage)
    }
}