package thierry.realestatemanager.model

import androidx.room.Embedded
import androidx.room.Relation

data class FullProperty(
    @Embedded val property: Property,
    @Relation(
        parentColumn = "id",
        entityColumn = "propertyId"
    )
    val mediaList: List<Media>,

    @Relation(
        parentColumn = "id",
        entityColumn = "propertyId"
    )
    val pointsOfInterestList: PointsOfInterest,
)