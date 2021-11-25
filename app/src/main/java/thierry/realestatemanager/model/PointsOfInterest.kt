package thierry.realestatemanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "points_of_interest_table")
data class PointsOfInterest(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val propertyId: Int,
    val school: String? = null,
    val park: String? = null,
    val shoppingCenter: String? = null
)