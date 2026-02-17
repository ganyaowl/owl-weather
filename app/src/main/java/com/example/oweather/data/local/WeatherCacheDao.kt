package com.example.oweather.data.local

import androidx.room.*

@Dao
interface WeatherCacheDao {
    @Query("SELECT * FROM weather_cache WHERE key = :key LIMIT 1")
    suspend fun get(key: String): WeatherCache?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun put(cache: WeatherCache)

    @Query("DELETE FROM weather_cache WHERE key = :key")
    suspend fun delete(key: String)
}
