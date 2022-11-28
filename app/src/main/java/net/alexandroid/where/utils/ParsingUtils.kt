package net.alexandroid.where.utils

import net.alexandroid.where.model.LatLng

object ParsingUtils {
    private var latTemp = ""
    private var lngTemp = ""

    fun handleLine(it: String): LatLng? {
        val line = it.trim()
        when {
            line.contains("latitudeE7") -> {
                val start = line.indexOf("E7") + 5
                latTemp = line.substring(start, line.length - 1)
            }

            line.contains("longitudeE7") -> {
                val start = line.indexOf("E7") + 5
                lngTemp = line.substring(start, line.length - 1)
            }

            line.contains("accuracy") -> {
                val start = line.indexOf(":") + 2
                val accuracy = line.substring(start, line.length - 1)
                val accuracyInt = if (accuracy.isNotEmpty()) accuracy.toInt() else 1000
                return LatLng(latTemp.toDegreeFormat(), lngTemp.toDegreeFormat(), accuracyInt)
            }
        }
        return null
    }
}