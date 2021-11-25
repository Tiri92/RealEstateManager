package thierry.realestatemanager.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import thierry.realestatemanager.model.Photo
import thierry.realestatemanager.model.Property
import thierry.realestatemanager.model.PropertyWithPhoto

@Dao
interface PropertyDao {

    @Query("SELECT * FROM property_table")
    fun getProperty(): Flow<List<Property>>

    @Query("SELECT MAX(id) + 1 FROM property_table")
    fun getLastIdPropertyTable(): Flow<Int>

    @Transaction
    @Query("SELECT * FROM property_table")
    fun getPropertyPhoto(): Flow<List<PropertyWithPhoto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProperty(property: Property)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPropertyPhoto(photo: Photo)

    @Update
    suspend fun update(property: Property)

}