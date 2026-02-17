package com.example.oweather.ui

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
class MainViewModel @Inject constructor(
    private val repo: WeatherRepository
) : ViewModel() {

    private val _forecast = MutableStateFlow<ForecastResponse?>(null)
    val forecast: StateFlow<ForecastResponse?> = _forecast

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun loadForecast(lat: Double, lon: Double) {
        viewModelScope.launch {
            _loading.value = true
            val f = repo.getForecast(lat, lon)
            _forecast.value = f
            _loading.value = false
        }
    }
}
