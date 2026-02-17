package com.example.oweather.network

import com.example.oweather.network.model.GeocodingResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingApi {
    @GET("v1/search")
    suspend fun search(@Query("name") name: String, @Query("count") count: Int = 10): GeocodingResponse
}
