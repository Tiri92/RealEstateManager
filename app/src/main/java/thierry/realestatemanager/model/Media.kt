package thierry.realestatemanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "media_table")
data class Media(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val propertyId: Int,
    val uri: String,
    var description: String,
    var position: Int? = null
)