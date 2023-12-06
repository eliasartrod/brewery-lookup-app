package com.example.brewerylookup.data

import com.example.brewerylookup.model.BreweryList
import com.example.brewerylookup.network.NetworkDataSource
import javax.inject.Inject

class BreweryLookupRepository @Inject constructor(
    private val dataSource: NetworkDataSource
) {

    suspend fun searchAllBreweries(
        amountPerPage: Int?
    ): Result<List<BreweryList>> {
        return dataSource.searchAllBreweries(amountPerPage)
            .map { BreweryList.fromNetwork(it) }
    }
}