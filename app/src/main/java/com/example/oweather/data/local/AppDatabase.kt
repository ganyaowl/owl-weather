package com.example.oweather.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [City::class, WeatherCache::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao
    abstract fun weatherCacheDao(): WeatherCacheDao
}
