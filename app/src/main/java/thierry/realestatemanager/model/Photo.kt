package thierry.realestatemanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photo_table")
data class Photo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val propertyId: Int,
    val uri: String,
    val photoName: String
)