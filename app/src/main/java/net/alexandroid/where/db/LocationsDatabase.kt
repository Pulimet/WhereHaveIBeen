package net.alexandroid.where.db

import androidx.room.Database
import androidx.room.RoomDatabase
import net.alexandroid.where.model.LatLngDb

@Database(entities = [LatLngDb::class], version = 1, exportSchema = true)
abstract class LocationsDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
}