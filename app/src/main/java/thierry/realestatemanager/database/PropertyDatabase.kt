package thierry.realestatemanager.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import thierry.realestatemanager.database.dao.PropertyDao
import thierry.realestatemanager.di.ApplicationScope
import thierry.realestatemanager.model.Address
import thierry.realestatemanager.model.Photo
import thierry.realestatemanager.model.Property
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Property::class, Photo::class], version = 1, exportSchema = false)
abstract class PropertyDatabase : RoomDatabase() {

    abstract fun propertyDao(): PropertyDao

    class Callback @Inject constructor(
        private val database: Provider<PropertyDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().propertyDao()

            applicationScope.launch {
                dao.insertProperty(Property(price = 17870000, address = Address(street = "31 Avenue André Morizet", city = "Paris"), type = "House", numberOfRooms = 3, numberOfBedrooms = 4, numberOfBathrooms = 2, surface = 435, description = "1How do you know exactly what you're buying when it comes to land? A legal description is a written record of a piece of land containing information that clearly identifies it. This description can be written in a few different forms depending on where the property is located in the United States. However, when dealing with one geographic area, the descriptions will tend to follow the same wording and language.A legal description provides legal evidence of the boundaries and allows a surveyor to accurately determine property lines at a later time. This is incredibly useful and necessary during real estate transactions or disputes. The description will typically appear on sales contracts and the property deed."))
                dao.insertProperty(Property(price = 8430000, address = Address(street = "31 Avenue André Morizet", city = "New York"), type = "Flat", numberOfRooms = 5, numberOfBedrooms = 7, numberOfBathrooms = 3, surface = 665, description = "2How do you know exactly what you're buying when it comes to land? A legal description is a written record of a piece of land containing information that clearly identifies it. This description can be written in a few different forms depending on where the property is located in the United States. However, when dealing with one geographic area, the descriptions will tend to follow the same wording and language.A legal description provides legal evidence of the boundaries and allows a surveyor to accurately determine property lines at a later time. This is incredibly useful and necessary during real estate transactions or disputes. The description will typically appear on sales contracts and the property deed."))
                dao.insertProperty(Property(price = 41650000, address = Address(street = "31 Avenue André Morizet", city = "London"), type = "Duplex", numberOfRooms = 4, numberOfBedrooms = 3, numberOfBathrooms = 3, surface = 480, description = "3How do you know exactly what you're buying when it comes to land? A legal description is a written record of a piece of land containing information that clearly identifies it. This description can be written in a few different forms depending on where the property is located in the United States. However, when dealing with one geographic area, the descriptions will tend to follow the same wording and language.A legal description provides legal evidence of the boundaries and allows a surveyor to accurately determine property lines at a later time. This is incredibly useful and necessary during real estate transactions or disputes. The description will typically appear on sales contracts and the property deed."))
            }
        }
    }

}