package com.example.oweather.data

import android.content.Context
import com.example.oweather.data.local.City
import com.example.oweather.data.local.CityDao
import com.example.oweather.data.local.WeatherCache
import com.example.oweather.data.local.WeatherCacheDao
import com.example.oweather.network.GeocodingApi
import com.example.oweather.network.WeatherApi
import com.example.oweather.network.model.ForecastResponse
import com.example.oweather.network.model.GeocodingResponse
import com.squareup.moshi.Moshi
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val weatherApi: WeatherApi,
    private val geocodingApi: GeocodingApi,
    private val cityDao: CityDao,
    private val cacheDao: WeatherCacheDao,
    private val moshi: Moshi,
    @ApplicationContext private val context: Context
) {

    fun getCities(): Flow<List<City>> = cityDao.getAll()

    suspend fun addCity(city: City): Long = cityDao.insert(city)

    suspend fun updateCity(city: City) = cityDao.update(city)

    suspend fun deleteCity(city: City) = cityDao.delete(city)

    suspend fun searchGeo(name: String): GeocodingResponse? {
        return try {
            geocodingApi.search(name)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getForecast(lat: Double, lon: Double): ForecastResponse? {
        val key = "${lat}_${lon}"
        return try {
            val resp = weatherApi.forecast(lat, lon)
            // cache as json
            val json = moshi.adapter(ForecastResponse::class.java).toJson(resp)
            cacheDao.put(WeatherCache(key = key, json = json))
            resp
        } catch (e: Exception) {
            // try cache
            val cached = cacheDao.get(key)
            if (cached != null) {
                return moshi.adapter(ForecastResponse::class.java).fromJson(cached.json)
            }
            // fallback asset
            return loadFallback()
        }
    }

    private suspend fun loadFallback(): ForecastResponse? = withContext(Dispatchers.IO) {
        return@withContext try {
            val json = context.assets.open("weather_fallback.json").bufferedReader().use { it.readText() }
            moshi.adapter(ForecastResponse::class.java).fromJson(json)
        } catch (e: Exception) {
            null
        }
    }
}
