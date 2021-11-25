package thierry.realestatemanager.model

import androidx.room.Embedded
import androidx.room.Relation

data class PropertyWithPointOfInterest(
    @Embedded val property: Property,
    @Relation(
        parentColumn = "id",
        entityColumn = "propertyId"
    )
    val pointsOfInterestList: List<PointsOfInterest>
)