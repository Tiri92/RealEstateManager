package thierry.realestatemanager.repositories

import thierry.realestatemanager.database.dao.PropertyDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDatabaseRepository @Inject constructor(private val propertyDao: PropertyDao) {

    fun allProperty() = propertyDao.getProperty()

    fun allPropertyPhoto() = propertyDao.getPropertyPhoto()

}