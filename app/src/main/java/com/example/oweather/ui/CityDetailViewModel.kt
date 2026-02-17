package com.example.oweather.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oweather.data.WeatherRepository
import com.example.oweather.network.model.ForecastResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityDetailViewModel @Inject constructor(
    private val repo: WeatherRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _forecast = MutableStateFlow<ForecastResponse?>(null)
    val forecast: StateFlow<ForecastResponse?> = _forecast

    init {
        val lat = savedStateHandle.get<String>("lat")?.toDoubleOrNull()
        val lon = savedStateHandle.get<String>("lon")?.toDoubleOrNull()
        if (lat != null && lon != null) load(lat, lon)
    }

    fun load(lat: Double, lon: Double) {
        viewModelScope.launch {
            val f = repo.getForecast(lat, lon)
            _forecast.value = f
        }
    }
}
