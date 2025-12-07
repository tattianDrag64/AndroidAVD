package com.example.weathernotification.data.repository

import com.example.weathernotification.data.dao.CityDao
import com.example.weathernotification.data.entity.CityEntity

//repository to manage city data from the local database
class CityRepository(
    private val cityDao: CityDao
) {

    //retrieves all cities from the database as a flow
    fun getAllCities() = cityDao.getAllCities()

    //adds a new city to the database
    suspend fun addCity(city: CityEntity) {
        cityDao.insertCity(city)
    }
}
