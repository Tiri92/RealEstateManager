package thierry.realestatemanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "video_table")
data class Video(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val propertyId: Int,
    val uri: String,
    var videoDescription: String
)