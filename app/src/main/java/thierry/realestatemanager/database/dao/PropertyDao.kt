package thierry.realestatemanager.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import thierry.realestatemanager.model.*

@Dao
interface PropertyDao {

    @Transaction
    @Query("SELECT * FROM property_table")
    fun getFullPropertyList(): Flow<List<FullProperty>>

    @Transaction
    @Query("SELECT * FROM property_table WHERE id = :id")
    fun getCurrentFullProperty(id: Int): Flow<FullProperty>

    @Query("SELECT * FROM property_table")
    fun getProperty(): Flow<List<Property>>

    @Query("SELECT MAX(id) + 1 FROM property_table")
    fun getLastIdPropertyTable(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProperty(property: Property)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPropertyMedia(media: Media)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPropertyPointsOfInterest(pointsOfInterest: PointsOfInterest)

    @Update
    suspend fun updateProperty(property: Property)

    @Update
    suspend fun updatePropertyPointOfInterest(pointsOfInterest: PointsOfInterest)

    @Delete
    suspend fun deletePropertyMedia(media: Media)

}