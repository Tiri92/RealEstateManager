package thierry.realestatemanager.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import thierry.realestatemanager.model.*

@Dao
interface PropertyDao {

    @Query("SELECT * FROM property_table")
    fun getProperty(): Flow<List<Property>>

    @Query("SELECT * FROM property_table WHERE id = :id")
    fun getActualProperty(id: Int): Flow<Property>

    @Query("SELECT MAX(id) + 1 FROM property_table")
    fun getLastIdPropertyTable(): Flow<Int>

    @Transaction
    @Query("SELECT * FROM property_table")
    fun getPropertyMedia(): Flow<List<PropertyWithMedia>>

//    @Transaction
//    @Query("SELECT * FROM points_of_interest_table")
//    fun getPropertyPointsOfInterest(): Flow<List<PropertyWithPointsOfInterest>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProperty(property: Property)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPropertyMedia(media: Media)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPropertyPointsOfInterest(pointsOfInterest: PointsOfInterest)

    @Update
    suspend fun update(property: Property)

}