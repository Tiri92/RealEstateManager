package thierry.realestatemanager.repositories

import thierry.realestatemanager.database.dao.PropertyDao
import thierry.realestatemanager.model.Media
import thierry.realestatemanager.model.Property
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDatabaseRepository @Inject constructor(private val propertyDao: PropertyDao) {

    fun allProperty() = propertyDao.getProperty()

    fun allPropertyMedia() = propertyDao.getPropertyMedia()

    fun getLastIdPropertyTable() = propertyDao.getLastIdPropertyTable()

    suspend fun insertPropertyMedia(media: Media) = propertyDao.insertPropertyMedia(media)

    suspend fun insertProperty(property: Property) = propertyDao.insertProperty(property)

}