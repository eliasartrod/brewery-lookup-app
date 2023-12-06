package com.example.brewerylookup.data

import com.example.brewerylookup.model.BreweryList
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
}