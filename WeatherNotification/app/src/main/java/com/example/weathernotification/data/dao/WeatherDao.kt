package com.example.weathernotification.data.dao

import androidx.room.*
import com.example.weathernotification.data.entity.WeatherEntity
import kotlinx.coroutines.flow.Flow

//data access object for weather entities
@Dao
interface WeatherDao {

    //inserts or replaces a weather entity
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity)

    //retrieves all weather entities, ordered by id descending
    @Query("SELECT * FROM weather_table ORDER BY id DESC")
    fun getAllWeather(): Flow<List<WeatherEntity>>

    //deletes a specific weather entity
    @Delete
    suspend fun deleteWeather(weather: WeatherEntity)

    //updates a specific weather entity
    @Update
    suspend fun updateWeather(weather: WeatherEntity)
}
