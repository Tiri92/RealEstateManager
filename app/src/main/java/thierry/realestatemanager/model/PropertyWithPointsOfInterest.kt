package thierry.realestatemanager.model

import androidx.room.Embedded
import androidx.room.Relation

data class PropertyWithPointsOfInterest(
    @Embedded val property: Property,
    @Relation(
        parentColumn = "id",
        entityColumn = "propertyId"
    )
    val pointsOfInterestList: List<PointsOfInterest>
)