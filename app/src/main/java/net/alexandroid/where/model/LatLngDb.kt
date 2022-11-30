package net.alexandroid.where.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "locations")
data class LatLngDb(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val lat: Double,
    val lng: Double,
    val countryName: String,
    val accuracy: Int,
    val source: String
) : Parcelable {
    companion object {
        fun create(latLng: LatLng, countryName: String) =
            LatLngDb(0, latLng.lat, latLng.lng, countryName, latLng.accuracy, latLng.src)
    }
}