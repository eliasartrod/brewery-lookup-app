package com.example.brewerylookup.network.retrofit

import com.example.brewerylookup.network.model.NetworkDirectionsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleMapsService {

    @GET("/maps/api/directions/json")
    suspend fun getDirections(
        @Query("destination") origin: String,
        @Query("origin") destination: String,
        @Query("key") apiKey: String
    ): Result<NetworkDirectionsResponse>
}