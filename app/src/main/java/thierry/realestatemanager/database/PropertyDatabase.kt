package thierry.realestatemanager.database

import android.annotation.SuppressLint
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import thierry.realestatemanager.database.dao.PropertyDao
import thierry.realestatemanager.di.ApplicationScope
import thierry.realestatemanager.model.*
import thierry.realestatemanager.utils.Utils
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Property::class, Media::class, PointsOfInterest::class],
    version = 1,
    exportSchema = false)
abstract class PropertyDatabase : RoomDatabase() {

    abstract fun propertyDao(): PropertyDao

    class Callback @Inject constructor(
        private val database: Provider<PropertyDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope,
    ) : RoomDatabase.Callback() {
        @SuppressLint("SdCardPath")
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().propertyDao()

            applicationScope.launch {

                dao.insertProperty(Property(price = 17870000,
                    address = Address(street = "31 Avenue André Morizet",
                        city = "Boulogne Billancourt",
                        postcode = 92100, country = "France"),
                    type = "House",
                    numberOfRooms = 3,
                    numberOfBedrooms = 4,
                    numberOfBathrooms = 2,
                    surface = 435,
                    dateOfCreation = Utils.getTodayDate(),
                    propertyAgent = "Eric",
                    description = "1How do you know exactly what you're buying when it comes to land? A legal description is a written record of a piece of land containing information that clearly identifies it. This description can be written in a few different forms depending on where the property is located in the United States. However, when dealing with one geographic area, the descriptions will tend to follow the same wording and language.A legal description provides legal evidence of the boundaries and allows a surveyor to accurately determine property lines at a later time. This is incredibly useful and necessary during real estate transactions or disputes. The description will typically appear on sales contracts and the property deed."))
                dao.insertProperty(Property(price = 8430000,
                    address = Address(street = "70 rue de l'égalité",
                        city = "Issy les Moulineaux",
                        postcode = 92130, country = "France"),
                    type = "Flat",
                    numberOfRooms = 5,
                    numberOfBedrooms = 7,
                    numberOfBathrooms = 3,
                    surface = 665,
                    dateOfCreation = Utils.getTodayDate(),
                    propertyAgent = "Mike",
                    description = "2How do you know exactly what you're buying when it comes to land? A legal description is a written record of a piece of land containing information that clearly identifies it. This description can be written in a few different forms depending on where the property is located in the United States. However, when dealing with one geographic area, the descriptions will tend to follow the same wording and language.A legal description provides legal evidence of the boundaries and allows a surveyor to accurately determine property lines at a later time. This is incredibly useful and necessary during real estate transactions or disputes. The description will typically appear on sales contracts and the property deed."))
                dao.insertProperty(Property(price = 41650000,
                    address = Address(street = "15 rue le marois",
                        city = "Paris",
                        postcode = 75016, country = "France"),
                    type = "Duplex",
                    numberOfRooms = 4,
                    numberOfBedrooms = 3,
                    numberOfBathrooms = 3,
                    surface = 480,
                    dateOfCreation = Utils.getTodayDate(),
                    propertyAgent = "Alex",
                    description = "3How do you know exactly what you're buying when it comes to land? A legal description is a written record of a piece of land containing information that clearly identifies it. This description can be written in a few different forms depending on where the property is located in the United States. However, when dealing with one geographic area, the descriptions will tend to follow the same wording and language.A legal description provides legal evidence of the boundaries and allows a surveyor to accurately determine property lines at a later time. This is incredibly useful and necessary during real estate transactions or disputes. The description will typically appear on sales contracts and the property deed."))
                dao.insertProperty(Property(price = 22650000,
                    address = Address(street = "100 Avenue André Morizet",
                        city = "Boulogne Billancourt",
                        postcode = 92100, country = "France"),
                    type = "House",
                    numberOfRooms = 4,
                    numberOfBedrooms = 3,
                    numberOfBathrooms = 3,
                    surface = 480,
                    dateOfCreation = Utils.getTodayDate(),
                    propertyAgent = "Eric",
                    description = "4How do you know exactly what you're buying when it comes to land? A legal description is a written record of a piece of land containing information that clearly identifies it. This description can be written in a few different forms depending on where the property is located in the United States. However, when dealing with one geographic area, the descriptions will tend to follow the same wording and language.A legal description provides legal evidence of the boundaries and allows a surveyor to accurately determine property lines at a later time. This is incredibly useful and necessary during real estate transactions or disputes. The description will typically appear on sales contracts and the property deed."))

                dao.insertPropertyPointsOfInterest(PointsOfInterest(school = true,
                    university = true,
                    propertyId = 1,
                    parks = true,
                    sportsClubs = false,
                    stations = true,
                    shoppingCenter = false))
                dao.insertPropertyPointsOfInterest(PointsOfInterest(school = true,
                    university = true,
                    propertyId = 2,
                    parks = true,
                    sportsClubs = false,
                    stations = true,
                    shoppingCenter = false))
                dao.insertPropertyPointsOfInterest(PointsOfInterest(school = true,
                    university = true,
                    propertyId = 3,
                    parks = true,
                    sportsClubs = false,
                    stations = true,
                    shoppingCenter = false))
                dao.insertPropertyPointsOfInterest(PointsOfInterest(school = true,
                    university = true,
                    propertyId = 4,
                    parks = true,
                    sportsClubs = false,
                    stations = true,
                    shoppingCenter = false))

                dao.insertPropertyMedia(Media(propertyId = 1, uri = "/data/user/0/thierry.realestatemanager/files/1Photo1.jpg", description = "Living Room"))
                dao.insertPropertyMedia(Media(propertyId = 1, uri = "/data/user/0/thierry.realestatemanager/files/1Photo2.jpg", description = "Living Room 2"))
                dao.insertPropertyMedia(Media(propertyId = 1, uri = "/data/user/0/thierry.realestatemanager/files/1Photo3.jpg", description = "Bedroom"))
                dao.insertPropertyMedia(Media(propertyId = 1, uri = "/data/user/0/thierry.realestatemanager/files/1Photo4.jpg", description = "Bathroom"))

                dao.insertPropertyMedia(Media(propertyId = 2, uri = "/data/user/0/thierry.realestatemanager/files/2Photo1.jpg", description = "Living Room"))
                dao.insertPropertyMedia(Media(propertyId = 2, uri = "/data/user/0/thierry.realestatemanager/files/2Photo2.jpg", description = "Bedroom"))
                dao.insertPropertyMedia(Media(propertyId = 2, uri = "/data/user/0/thierry.realestatemanager/files/2Photo3.jpg", description = "Bedroom 2"))
                dao.insertPropertyMedia(Media(propertyId = 2, uri = "/data/user/0/thierry.realestatemanager/files/2Photo4.jpg", description = "Kitchen"))
                dao.insertPropertyMedia(Media(propertyId = 2, uri = "/data/user/0/thierry.realestatemanager/files/2Photo5.jpg", description = "Bathroom"))
                dao.insertPropertyMedia(Media(propertyId = 2, uri = "/data/user/0/thierry.realestatemanager/files/2Photo6.jpg", description = "Bathroom 2"))
                dao.insertPropertyMedia(Media(propertyId = 2, uri = "/data/user/0/thierry.realestatemanager/files/2Photo7.jpg", description = "Sauna"))

                dao.insertPropertyMedia(Media(propertyId = 3, uri = "/data/user/0/thierry.realestatemanager/files/3Photo1.jpg", description = "Living Room"))
                dao.insertPropertyMedia(Media(propertyId = 3, uri = "/data/user/0/thierry.realestatemanager/files/3Photo2.jpg", description = "Living Room 2"))
                dao.insertPropertyMedia(Media(propertyId = 3, uri = "/data/user/0/thierry.realestatemanager/files/3Photo3.jpg", description = "Living Room 3"))
                dao.insertPropertyMedia(Media(propertyId = 3, uri = "/data/user/0/thierry.realestatemanager/files/3Photo4.jpg", description = "Kitchen"))
                dao.insertPropertyMedia(Media(propertyId = 3, uri = "/data/user/0/thierry.realestatemanager/files/3Photo5.jpg", description = "Bedroom"))
                dao.insertPropertyMedia(Media(propertyId = 3, uri = "/data/user/0/thierry.realestatemanager/files/3Photo6.jpg", description = "Bathroom"))

                dao.insertPropertyMedia(Media(propertyId = 4, uri = "/data/user/0/thierry.realestatemanager/files/4Photo1.jpg", description = "Living Room"))
                dao.insertPropertyMedia(Media(propertyId = 4, uri = "/data/user/0/thierry.realestatemanager/files/4Photo2.jpg", description = "Kitchen"))
                dao.insertPropertyMedia(Media(propertyId = 4, uri = "/data/user/0/thierry.realestatemanager/files/4Photo3.jpg", description = "Bedroom"))
                dao.insertPropertyMedia(Media(propertyId = 4, uri = "/data/user/0/thierry.realestatemanager/files/4Photo4.jpg", description = "Bedroom 2"))
                dao.insertPropertyMedia(Media(propertyId = 4, uri = "/data/user/0/thierry.realestatemanager/files/4Photo5.jpg", description = "Bathroom"))
                dao.insertPropertyMedia(Media(propertyId = 4, uri = "/data/user/0/thierry.realestatemanager/files/4Photo6.jpg", description = "Bathroom 2"))

            }
        }
    }

}