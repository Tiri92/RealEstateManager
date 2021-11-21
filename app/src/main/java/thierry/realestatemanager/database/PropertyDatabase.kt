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
                dao.insertProperty(Property(price = 17870000, address = Address(street = "31 Avenue André Morizet", city = "Paris"), type = "House"))
                dao.insertProperty(Property(price = 8430000, address = Address(street = "31 Avenue André Morizet", city = "New York"), type = "Flat"))
                dao.insertProperty(Property(price = 41650000, address = Address(street = "31 Avenue André Morizet", city = "London"), type = "Duplex"))
                dao.insertPropertyPhoto(Photo(propertyId = 3, uri = "test", photoName = "ça marche!"))
                dao.insertPropertyPhoto(Photo(propertyId = 3, uri = "test", photoName = "ça marche encore plus!"))
            }
        }
    }

}