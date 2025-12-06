package com.example.weathernotification.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weathernotification.data.dao.CityDao
import com.example.weathernotification.data.entity.CityEntity

@Database(
    entities = [CityEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun cityDao(): CityDao
}