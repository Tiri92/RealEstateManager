package thierry.realestatemanager.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import thierry.realestatemanager.model.Property

@Dao
interface PropertyDao {

    @Query("SELECT * FROM property_table")
    fun getProperty(): Flow<List<Property>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(property: Property)

    @Update
    suspend fun update(property: Property)

}