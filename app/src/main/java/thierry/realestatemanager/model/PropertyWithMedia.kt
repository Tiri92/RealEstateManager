package thierry.realestatemanager.model

import androidx.room.Embedded
import androidx.room.Relation

data class PropertyWithMedia(
    @Embedded val property: Property,
    @Relation(
        parentColumn = "id",
        entityColumn = "propertyId"
    )
    val mediaList: List<Media>
)