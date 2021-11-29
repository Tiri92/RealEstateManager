package thierry.realestatemanager.repositories

import thierry.realestatemanager.database.dao.PropertyDao
import thierry.realestatemanager.model.Media
import thierry.realestatemanager.model.PointsOfInterest
import thierry.realestatemanager.model.Property
import thierry.realestatemanager.model.PropertyWithPointsOfInterest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDatabaseRepository @Inject constructor(private val propertyDao: PropertyDao) {

    fun allProperty() = propertyDao.getProperty()

    fun allPropertyMedia() = propertyDao.getPropertyMedia()

    //fun allPropertyPointsOfInterest() = propertyDao.getPropertyPointsOfInterest()

    fun getLastIdPropertyTable() = propertyDao.getLastIdPropertyTable()

    suspend fun insertPropertyMedia(media: Media) = propertyDao.insertPropertyMedia(media)

    suspend fun insertProperty(property: Property) = propertyDao.insertProperty(property)

    suspend fun insertPointsOfInterest(pointsOfInterest: PointsOfInterest) = propertyDao.insertPropertyPointsOfInterest(pointsOfInterest)

}