package com.example.brewerylookup.data

import com.example.brewerylookup.model.BreweryList
import com.example.brewerylookup.model.Directions
import com.example.brewerylookup.network.NetworkDataSource
import javax.inject.Inject

class BreweryLookupRepository @Inject constructor(
    private val dataSource: NetworkDataSource
) {

    suspend fun searchAllBreweries(
        pageNumber: Int,
        amountPerPage: Int?
    ): Result<List<BreweryList>> {
        return dataSource.searchAllBreweries(pageNumber, amountPerPage)
            .map { BreweryList.fromNetwork(it) }
    }

    suspend fun searchByFilter(
        breweryType: String?,
        state: String?,
        postalCode: String?,
        city: String?,
        breweryName: String?
    ): Result<List<BreweryList>> {
        return dataSource.searchByFilter(breweryType, state, postalCode, city, breweryName)
            .map { BreweryList.fromNetwork(it) }
    }

    suspend fun getDirections(
        startingAddress: String,
        destinationAddress: String,
        apiKey: String
    ): Result<Directions?> {
        return dataSource.getDirections(startingAddress, destinationAddress, apiKey)
            .map { Directions.fromNetwork(it) }
    }
}