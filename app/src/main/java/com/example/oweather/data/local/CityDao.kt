package com.example.oweather.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {
    @Query("SELECT * FROM cities ORDER BY createdAt DESC")
    fun getAll(): Flow<List<City>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(city: City): Long

    @Update
    suspend fun update(city: City)

    @Delete
    suspend fun delete(city: City)
}
