package com.example.oweather.network.model

import com.squareup.moshi.Json

data class GeocodingResponse(
    @Json(name = "results") val results: List<GeoResult>?
)

data class GeoResult(
    val id: Long?,
    val name: String?,
    val latitude: Double?,
    val longitude: Double?,
    val country: String?
)
