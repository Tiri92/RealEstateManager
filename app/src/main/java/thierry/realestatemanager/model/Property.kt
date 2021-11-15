package thierry.realestatemanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "property_table")
data class Property(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val price: Int

)