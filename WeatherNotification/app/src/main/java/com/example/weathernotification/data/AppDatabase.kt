package com.example.weathernotification.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weathernotification.data.dao.CityDao
import com.example.weathernotification.data.dao.WeatherDao
import com.example.weathernotification.data.entity.CityEntity
import com.example.weathernotification.data.entity.WeatherEntity

@Database(
    entities = [WeatherEntity::class, CityEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao
    abstract fun cityDao(): CityDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "weather_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance

            }
        }
    }
}
