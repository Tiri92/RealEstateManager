package thierry.realestatemanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "points_of_interest_table")
data class PointsOfInterest(
    @PrimaryKey val propertyId: Int,
    val school: Boolean,
    val university: Boolean,
    val parks: Boolean,
    val sportsClubs: Boolean,
    val stations: Boolean,
    val shoppingCenter: Boolean
)