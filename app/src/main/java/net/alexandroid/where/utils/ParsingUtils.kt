package net.alexandroid.where.utils

import net.alexandroid.where.model.LatLng

object ParsingUtils {
    private var latTemp = ""
    private var lngTemp = ""
    private var accuracyIntTemp = 1000

    fun handleLine(it: String): LatLng? {
        val line = it.trim()
        when {
            line.contains("latitudeE7") -> latTemp = getValue(line)
            line.contains("longitudeE7") -> lngTemp = getValue(line)
            line.contains("accuracy") -> {
                val accuracy = getValue(line)
                accuracyIntTemp = if (accuracy.isNotEmpty()) accuracy.toInt() else 1000
            }
            line.contains("source") -> {
                val source = getValueString(line)
                return LatLng(
                    latTemp.toDegreeFormat(),
                    lngTemp.toDegreeFormat(),
                    accuracyIntTemp,
                    source
                )
            }

        }
        return null
    }

    private fun getValue(line: String): String {
        val start = line.indexOf(":") + 2
        return line.substring(start, line.length - 1)
    }
    private fun getValueString(line: String): String {
        val start = line.indexOf(":") + 3
        return line.substring(start, line.length - 2)
    }
}