package thierry.realestatemanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "points_of_interest_table")
data class PointsOfInterest(
    @PrimaryKey val propertyId: Int,
    val school: Boolean? = null,
    val university: Boolean? = null,
    val parks: Boolean? = null,
    val sportsClubs: Boolean? = null,
    val stations: Boolean? = null,
    val shoppingCenter: Boolean? = null
)