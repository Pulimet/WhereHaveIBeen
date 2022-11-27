package net.alexandroid.where.repo

import net.alexandroid.where.db.LocationDao
import net.alexandroid.where.model.Location
import net.alexandroid.where.utils.logs.logE

class LocationsRepo(private val locationDao: LocationDao) {

    private suspend fun add(location: Location) {
        try {
            locationDao.insert(location)
        } catch (e: Exception) {
            logE("Failed to add location to the DB: $location", t = e)
        }
    }
}