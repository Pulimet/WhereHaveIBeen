package net.alexandroid.where.db

import androidx.room.Database
import androidx.room.RoomDatabase
import net.alexandroid.where.model.Location

@Database(entities = [Location::class], version = 1, exportSchema = true)
abstract class LocationsDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
}