package net.alexandroid.where.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import net.alexandroid.where.model.LatLngDb

@Dao
interface LocationDao {
    @Query("SELECT * from locations")
    fun getLocations(): Flow<List<LatLngDb>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(location: LatLngDb)
}