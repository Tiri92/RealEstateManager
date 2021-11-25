package thierry.realestatemanager.repositories

import thierry.realestatemanager.database.dao.PropertyDao
import thierry.realestatemanager.model.Photo
import thierry.realestatemanager.model.Property
import thierry.realestatemanager.model.Video
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDatabaseRepository @Inject constructor(private val propertyDao: PropertyDao) {

    fun allProperty() = propertyDao.getProperty()

    fun allPropertyPhoto() = propertyDao.getPropertyPhoto()

    fun getLastIdPropertyTable() = propertyDao.getLastIdPropertyTable()

    suspend fun insertPropertyPhoto(photo: Photo) = propertyDao.insertPropertyPhoto(photo)

    suspend fun insertPropertyVideo(video: Video) = propertyDao.insertPropertyVideo(video)

    suspend fun insertProperty(property: Property) = propertyDao.insertProperty(property)

}