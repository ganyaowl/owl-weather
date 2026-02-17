package com.example.oweather.network

import com.example.oweather.network.model.ForecastResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("v1/forecast")
    suspend fun forecast(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("daily") daily: String = "temperature_2m_max,temperature_2m_min,weathercode",
        @Query("current_weather") current: Boolean = true,
        @Query("timezone") timezone: String = "auto"
    ): ForecastResponse
}
