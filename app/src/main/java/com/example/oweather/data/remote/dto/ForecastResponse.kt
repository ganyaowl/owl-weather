package com.example.oweather.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ForecastResponse(
    val latitude: Double,
    val longitude: Double,
    val current: CurrentDto?,
    @SerializedName("current_weather")
    val currentWeather: LegacyCurrentWeatherDto?,
    val daily: DailyDto?
)

data class CurrentDto(
    @SerializedName("temperature_2m")
    val temperature: Double?,
    @SerializedName("wind_speed_10m")
    val windSpeed: Double?,
    @SerializedName("weather_code")
    val weatherCode: Int?
)

data class LegacyCurrentWeatherDto(
    val temperature: Double?,
    val windspeed: Double?,
    val weathercode: Int?
)

data class DailyDto(
    val time: List<String>?,
    @SerializedName("temperature_2m_max")
    val temperatureMax: List<Double>?,
    @SerializedName("temperature_2m_min")
    val temperatureMin: List<Double>?,
    @SerializedName("weather_code")
    val weatherCode: List<Int>?,
    val weathercode: List<Int>?
)
