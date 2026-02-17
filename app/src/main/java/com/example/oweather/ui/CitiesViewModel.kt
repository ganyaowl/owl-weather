package com.example.oweather.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oweather.data.WeatherRepository
import com.example.oweather.data.local.City
import com.example.oweather.network.model.GeoResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CitiesViewModel @Inject constructor(
    private val repo: WeatherRepository
) : ViewModel() {

    val cities: StateFlow<List<City>> = repo.getCities()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    suspend fun addCity(name: String, lat: Double, lon: Double) {
        repo.addCity(City(name = name, latitude = lat, longitude = lon))
    }

    fun deleteCity(city: City) {
        viewModelScope.launch { repo.deleteCity(city) }
    }

    suspend fun search(name: String): List<GeoResult> {
        val res = repo.searchGeo(name)
        return res?.results ?: emptyList()
    }
}
