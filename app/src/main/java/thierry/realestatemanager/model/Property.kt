package thierry.realestatemanager.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "property_table")
data class Property(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val price: Int? = null,
    @Embedded
    val address: Address? = null,
    val type: String? = null,
    val isSold: Boolean? = null,
    val numberOfRooms: Int? = null,
    val numberOfBedrooms: Int? = null,
    val numberOfBathrooms: Int? = null,
    val surface: Int? = null,
    val description: String? = null,
    val staticMapUri: String? = null



)