package com.example.weathernotification.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathernotification.data.entity.WeatherEntity
import com.example.weathernotification.data.repository.WeatherRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    val savedWeather = repository
        .getSavedWeather()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun loadWeather(city: String) {
        viewModelScope.launch {
            val response = repository.loadWeatherFromApi(city)

            val entity = WeatherEntity(
                city = response.name,
                temp = response.main.temp,
                wind = response.wind.speed,
                time = System.currentTimeMillis().toString()
            )

            repository.saveWeatherToDb(entity)
        }
    }

    fun deleteWeather(weather: WeatherEntity) {
        viewModelScope.launch {
            repository.deleteWeather(weather)
        }
    }
}