package com.example.weathernotification.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathernotification.data.database.DatabaseProvider
import com.example.weathernotification.data.entity.CityEntity
import kotlinx.coroutines.launch

class CityViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DatabaseProvider.provideCityRepository(application)

    val cities = repository.getAllCities()

    fun addCity(name: String) {
        viewModelScope.launch {
            repository.addCity(
                CityEntity(name = name)
            )
        }
    }
}
