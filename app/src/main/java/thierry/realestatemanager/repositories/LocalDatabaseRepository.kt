package thierry.realestatemanager.repositories

import thierry.realestatemanager.database.dao.PropertyDao
import thierry.realestatemanager.model.Media
import thierry.realestatemanager.model.PointsOfInterest
import thierry.realestatemanager.model.Property
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDatabaseRepository @Inject constructor(private val propertyDao: PropertyDao) {

    fun getFullPropertyList() = propertyDao.getFullPropertyList()

    fun getCurrentFullProperty(id: Int) = propertyDao.getCurrentFullProperty(id)

    fun getLastIdPropertyTable() = propertyDao.getLastIdPropertyTable()

    suspend fun updateProperty(property: Property) = propertyDao.updateProperty(property)

    suspend fun updatePropertyPointOfInterest(pointsOfInterest: PointsOfInterest) =
        propertyDao.updatePropertyPointOfInterest(pointsOfInterest)

    suspend fun insertProperty(property: Property) = propertyDao.insertProperty(property)

    suspend fun insertPropertyMedia(media: Media) = propertyDao.insertPropertyMedia(media)

    suspend fun insertPointsOfInterest(pointsOfInterest: PointsOfInterest) =
        propertyDao.insertPropertyPointsOfInterest(pointsOfInterest)

    suspend fun deletePropertyMedia(media: Media) = propertyDao.deletePropertyMedia(media)

}