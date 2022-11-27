package net.alexandroid.where.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import net.alexandroid.where.utils.logs.logE

@Parcelize
@Entity(tableName = "locations")
data class Location(
    @PrimaryKey val id: Int,
    val latitudeE7: String,
    val longitudeE7: String,
    val accuracy: Int?,
    val source: String?,
    val timestamp: String
) : Parcelable {
    companion object {
        fun convertStringToObject(gson: Gson, location: String) = try {
            gson.fromJson(location, Location::class.java)
        } catch (e: Exception) {
            logE("Failed to parse JSON: $location")
            null
        }
    }
}
