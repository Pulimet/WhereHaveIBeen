package net.alexandroid.where.repo

import com.google.gson.Gson
import net.alexandroid.where.db.LocationDao
import net.alexandroid.where.model.Location
import net.alexandroid.where.utils.logs.logE

class LocationsRepo(private val gson: Gson, private val locationDao: LocationDao) {
    suspend fun add(location: String) {
        try {
            addToDb(gson.fromJson(location, Location::class.java))
        } catch (e: Exception) {
            logE("Failed to parse JSON: $location", t = e)
        }
    }

    private suspend fun addToDb(location: Location) {
        try {
            locationDao.insert(location)
        } catch (e: Exception) {
            logE("Failed to add location to the DB: $location", t = e)
        }
    }
}