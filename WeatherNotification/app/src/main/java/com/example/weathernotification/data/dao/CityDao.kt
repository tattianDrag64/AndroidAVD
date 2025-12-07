package com.example.weathernotification.data.dao

import androidx.room.*
import com.example.weathernotification.data.entity.CityEntity
import kotlinx.coroutines.flow.Flow

//data access object for city entities
@Dao
interface CityDao {

    //inserts or replaces a city entity
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(city: CityEntity)

    //deletes a specific city entity
    @Delete
    suspend fun deleteCity(city: CityEntity)

    //retrieves all city entities
    @Query("SELECT * FROM cities")
    fun getAllCities(): Flow<List<CityEntity>>
}
