package thierry.realestatemanager.model

import androidx.room.Embedded
import androidx.room.Relation

data class PropertyWithPhoto(
    @Embedded val property: Property,
    @Relation(
        parentColumn = "id",
        entityColumn = "propertyId"
    )
    val photolist: List<Photo>
)