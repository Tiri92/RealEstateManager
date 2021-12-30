package thierry.realestatemanager

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import thierry.realestatemanager.database.PropertyDatabase
import thierry.realestatemanager.database.dao.PropertyDao
import thierry.realestatemanager.model.Property

@RunWith(AndroidJUnit4::class)
class RoomDbInstrumentedTest {

    private lateinit var propertyDao: PropertyDao
    private lateinit var db: PropertyDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, PropertyDatabase::class.java).allowMainThreadQueries().build()
        propertyDao = db.propertyDao()
    }

    @Test
    @Throws(Exception::class)
    fun insertPropertiesAndGetThem() = runBlocking {
        val property1 = Property(numberOfRooms = 1)
        val property2 = Property(numberOfRooms = 2)
        val property3 = Property(numberOfRooms = 3)
        propertyDao.insertProperty(property1)
        propertyDao.insertProperty(property2)
        propertyDao.insertProperty(property3)
        val listOfAllProperty = propertyDao.getProperty().take(1)
        assertEquals(3, listOfAllProperty.first().size)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        db.close()
    }

}