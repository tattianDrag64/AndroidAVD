package com.example.weathernotification.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weathernotification.data.dao.CityDao
import com.example.weathernotification.data.dao.WeatherDao
import com.example.weathernotification.data.entity.WeatherEntity
import com.example.weathernotification.data.entity.CityEntity

//main database class for the application
@Database(
    entities = [WeatherEntity::class, CityEntity::class], //list of tables in the database
    version = 3, //database version, must be incremented on schema changes
    exportSchema = false //disabling schema export
)
abstract class AppDatabase : RoomDatabase() {

    //abstract function to get the weather dao
    abstract fun weatherDao(): WeatherDao
    //abstract function to get the city dao
    abstract fun cityDao(): CityDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        //singleton pattern to get a single instance of the database
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "weather_db" //database file name
                ).fallbackToDestructiveMigration().build() //strategy to recreate the db on version change
                INSTANCE = instance
                instance

            }
        }
    }
}