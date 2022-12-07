package net.alexandroid.where.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import net.alexandroid.where.model.LatLngDb

@Database(entities = [LatLngDb::class], version = 1, exportSchema = true)
abstract class LocationsDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao

    companion object {
        fun build(androidContext: Context) =
            Room.databaseBuilder(androidContext, LocationsDatabase::class.java, "locations_database").build()
    }
}