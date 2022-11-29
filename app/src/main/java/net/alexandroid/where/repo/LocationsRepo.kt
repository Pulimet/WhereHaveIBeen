package net.alexandroid.where.repo

import net.alexandroid.where.db.LocationDao
import net.alexandroid.where.model.LatLngDb
import net.alexandroid.where.utils.logs.logE

class LocationsRepo(private val locationDao: LocationDao) {
    suspend fun add(latLng: LatLngDb) {
        try {
            locationDao.insert(latLng)
        } catch (e: Exception) {
            logE("Failed to add location to the DB: $latLng", t = e)
        }
    }
}