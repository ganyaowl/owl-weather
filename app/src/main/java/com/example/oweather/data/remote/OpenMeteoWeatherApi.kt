package com.example.oweather.data.remote

import com.example.oweather.data.remote.dto.ForecastResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenMeteoWeatherApi {
    @GET("v1/forecast")
    suspend fun getForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String = "temperature_2m,wind_speed_10m,weather_code",
        @Query("daily") daily: String = "temperature_2m_max,temperature_2m_min,weather_code",
        @Query("timezone") timezone: String = "auto",
        @Query("wind_speed_unit") windSpeedUnit: String = "ms",
        @Query("forecast_days") forecastDays: Int = 7
    ): ForecastResponse
}
