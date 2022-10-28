package net.alexandroid.where.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "locations")
data class Location(
    @PrimaryKey
    val id: Int,
    val latitudeE7: Int,
    val longitudeE7: Int,
    val accuracy: Int?,
    val source: String?,
    val timestamp: String
) : Parcelable