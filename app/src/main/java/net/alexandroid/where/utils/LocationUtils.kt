package net.alexandroid.where.utils

import android.location.Geocoder
import net.alexandroid.where.model.LatLng
import net.alexandroid.where.model.LatLngDb
import net.alexandroid.where.utils.logs.logD
import net.alexandroid.where.utils.logs.logE
import kotlin.math.roundToInt

class LocationUtils(private val geocoder: Geocoder) {

    private val setOfCountries = mutableSetOf<String>()
    val countries = setOfCountries as Set<String>

    private val setOfCoordinates = mutableSetOf<String>()

    fun getCountryByCoordinates(latLng: LatLng): LatLngDb? {
        //logD("latLng: $latLng")
        //val roundLat = (latLng.lat * 10.0).roundToInt() / 10.0
        //val roundLng = (latLng.lng * 10.0).roundToInt() / 10.0
        val roundLng = latLng.lat.roundToInt()
        val roundLat = latLng.lng.roundToInt()
        val key = "${roundLat}_${roundLng}"
        //logD("key: $key")

        if (setOfCoordinates.contains(key)) {
            return null
        }
        //logD("It is a new key: $key")
        setOfCoordinates.add(key)

        return tryToGetCountryName(latLng)
    }

    @Suppress("DEPRECATION")
    private fun tryToGetCountryName(latLng: LatLng): LatLngDb? {
        try {
            val addressesList = geocoder.getFromLocation(latLng.lat, latLng.lng, 1)
            if (!addressesList.isNullOrEmpty()) {
                val countryName = addressesList[0].countryName ?: ""
                if (countryName.isNotEmpty() && !setOfCountries.contains(countryName)) {
                    logD("country: $countryName (latLng: $latLng)")
                    setOfCountries.add(countryName)
                    return LatLngDb.create(latLng, countryName)
                }
            }
        } catch (e: Exception) {
            logE("Failed to get location based on coordinates", t = e)
        }
        return null
    }
}