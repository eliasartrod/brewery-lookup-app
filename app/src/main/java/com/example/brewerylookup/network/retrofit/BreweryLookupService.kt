package com.example.brewerylookup.network.retrofit

import com.example.brewerylookup.network.model.NetworkBreweryListResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface BreweryLookupService {

    @GET("/v1/breweries/")
    suspend fun searchAllBreweries(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int?
    ): Result<List<NetworkBreweryListResponse>>

    @GET("/v1/breweries")
    suspend fun searchByFilter(
        @QueryMap options: Map<String, String>
    ): Result<List<NetworkBreweryListResponse>>
}