package thierry.realestatemanager

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import thierry.realestatemanager.database.PropertyDatabase
import thierry.realestatemanager.database.dao.PropertyDao
import thierry.realestatemanager.model.Media
import thierry.realestatemanager.model.PointsOfInterest
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

    @Test
    fun updateProperties() = runBlocking {
        val property1 = Property(id = 1, numberOfRooms = 1)
        val property2 = Property(id = 2, numberOfRooms = 2)
        val property3 = Property(id = 3, numberOfRooms = 3)
        propertyDao.insertProperty(property1)
        propertyDao.insertProperty(property2)
        propertyDao.insertProperty(property3)
        propertyDao.updateProperty(Property(id = 1, numberOfRooms = 10))
        propertyDao.updateProperty(Property(id = 2, numberOfRooms = 20))
        propertyDao.updateProperty(Property(id = 3, numberOfRooms = 30))
        val listOfAllProperty = propertyDao.getProperty().take(1)
        assertEquals(10, listOfAllProperty.first()[0].numberOfRooms)
        assertEquals(20, listOfAllProperty.first()[1].numberOfRooms)
        assertEquals(30, listOfAllProperty.first()[2].numberOfRooms)
    }

    @Test
    fun insertMediaAndGetThem() = runBlocking {
        val property1 = Property(id = 1, numberOfRooms = 1)
        propertyDao.insertProperty(property1)
        val pointsOfInterest1 = PointsOfInterest(id = 1,
            propertyId = 1,
            school = true,
            shoppingCenter = true,
            stations = true,
            sportsClubs = true,
            parks = false,
            university = false)
        propertyDao.insertPropertyPointsOfInterest(pointsOfInterest1)
        val media1 = Media(id = 1, description = "first", propertyId = 1, uri = "uri1")
        val media2 = Media(id = 2, description = "second", propertyId = 1, uri = "uri2")
        val media3 = Media(id = 3, description = "third", propertyId = 1, uri = "uri3")
        propertyDao.insertPropertyMedia(media1)
        propertyDao.insertPropertyMedia(media2)
        propertyDao.insertPropertyMedia(media3)
        val mediaListOfProperty1 = propertyDao.getCurrentFullProperty(1).first()
        assertEquals(3, mediaListOfProperty1.mediaList.size)
        assertEquals("first", mediaListOfProperty1.mediaList[0].description)
        assertEquals("second", mediaListOfProperty1.mediaList[1].description)
        assertEquals("third", mediaListOfProperty1.mediaList[2].description)
    }

    @Test
    fun updateMedia() = runBlocking {
        val property1 = Property(id = 1, numberOfRooms = 1)
        propertyDao.insertProperty(property1)
        val pointsOfInterest1 = PointsOfInterest(id = 1,
            propertyId = 1,
            school = true,
            shoppingCenter = true,
            stations = true,
            sportsClubs = true,
            parks = false,
            university = false)
        propertyDao.insertPropertyPointsOfInterest(pointsOfInterest1)
        val media1 = Media(id = 1, description = "first", propertyId = 1, uri = "uri1")
        val media2 = Media(id = 2, description = "second", propertyId = 1, uri = "uri2")
        val media3 = Media(id = 3, description = "third", propertyId = 1, uri = "uri3")
        propertyDao.insertPropertyMedia(media1)
        propertyDao.insertPropertyMedia(media2)
        propertyDao.insertPropertyMedia(media3)
        var mediaListOfProperty1 = propertyDao.getCurrentFullProperty(1).first()
        assertEquals("first", mediaListOfProperty1.mediaList[0].description)
        assertEquals("second", mediaListOfProperty1.mediaList[1].description)
        assertEquals("third", mediaListOfProperty1.mediaList[2].description)
        propertyDao.insertPropertyMedia(Media(id = 1,
            description = "first change",
            propertyId = 1,
            uri = "uri1"))
        propertyDao.insertPropertyMedia(Media(id = 2,
            description = "second change",
            propertyId = 1,
            uri = "uri2"))
        propertyDao.insertPropertyMedia(Media(id = 3,
            description = "third change",
            propertyId = 1,
            uri = "uri3"))
        mediaListOfProperty1 = propertyDao.getCurrentFullProperty(1).first()
        assertEquals("first change", mediaListOfProperty1.mediaList[0].description)
        assertEquals("second change", mediaListOfProperty1.mediaList[1].description)
        assertEquals("third change", mediaListOfProperty1.mediaList[2].description)
    }

    @Test
    fun deleteMedia() = runBlocking {
        val property1 = Property(id = 1, numberOfRooms = 1)
        propertyDao.insertProperty(property1)
        val pointsOfInterest1 = PointsOfInterest(id = 1,
            propertyId = 1,
            school = true,
            shoppingCenter = true,
            stations = true,
            sportsClubs = true,
            parks = false,
            university = false)
        propertyDao.insertPropertyPointsOfInterest(pointsOfInterest1)
        val media1 = Media(id = 1, description = "first", propertyId = 1, uri = "uri1")
        val media2 = Media(id = 2, description = "second", propertyId = 1, uri = "uri2")
        val media3 = Media(id = 3, description = "third", propertyId = 1, uri = "uri3")
        propertyDao.insertPropertyMedia(media1)
        propertyDao.insertPropertyMedia(media2)
        propertyDao.insertPropertyMedia(media3)
        var mediaListOfProperty1 = propertyDao.getCurrentFullProperty(1).first()
        assertEquals(3, mediaListOfProperty1.mediaList.size)
        propertyDao.deletePropertyMedia(media3)
        propertyDao.deletePropertyMedia(media2)
        mediaListOfProperty1 = propertyDao.getCurrentFullProperty(1).first()
        assertEquals(1, mediaListOfProperty1.mediaList.size)
    }

    @Test
    fun insertPointsOfInterestAndGetThem() = runBlocking {
        val property1 = Property(id = 1, numberOfRooms = 1)
        propertyDao.insertProperty(property1)
        val media1 = Media(id = 1, description = "first", propertyId = 1, uri = "uri1")
        propertyDao.insertPropertyMedia(media1)
        val pointsOfInterest1 = PointsOfInterest(id = 1,
            propertyId = 1,
            school = true,
            shoppingCenter = true,
            stations = true,
            sportsClubs = true,
            parks = false,
            university = false)
        propertyDao.insertPropertyPointsOfInterest(pointsOfInterest1)
        val pointsOfInterestOfProperty1 = propertyDao.getCurrentFullProperty(1).first()
        assertTrue(pointsOfInterestOfProperty1.pointsOfInterestList.school)
        assertTrue(pointsOfInterestOfProperty1.pointsOfInterestList.shoppingCenter)
        assertTrue(pointsOfInterestOfProperty1.pointsOfInterestList.stations)
        assertTrue(pointsOfInterestOfProperty1.pointsOfInterestList.sportsClubs)
        assertFalse(pointsOfInterestOfProperty1.pointsOfInterestList.parks)
        assertFalse(pointsOfInterestOfProperty1.pointsOfInterestList.university)
    }

    @Test
    fun updatePointsOfInterest() = runBlocking {
        val property1 = Property(id = 1, numberOfRooms = 1)
        propertyDao.insertProperty(property1)
        val media1 = Media(id = 1, description = "first", propertyId = 1, uri = "uri1")
        propertyDao.insertPropertyMedia(media1)
        val pointsOfInterest1 = PointsOfInterest(id = 1,
            propertyId = 1,
            school = true,
            shoppingCenter = true,
            stations = true,
            sportsClubs = true,
            parks = false,
            university = false)
        propertyDao.insertPropertyPointsOfInterest(pointsOfInterest1)
        var pointsOfInterestOfProperty1 = propertyDao.getCurrentFullProperty(1).first()
        assertTrue(pointsOfInterestOfProperty1.pointsOfInterestList.school)
        assertTrue(pointsOfInterestOfProperty1.pointsOfInterestList.shoppingCenter)
        assertTrue(pointsOfInterestOfProperty1.pointsOfInterestList.stations)
        assertTrue(pointsOfInterestOfProperty1.pointsOfInterestList.sportsClubs)
        assertFalse(pointsOfInterestOfProperty1.pointsOfInterestList.parks)
        assertFalse(pointsOfInterestOfProperty1.pointsOfInterestList.university)
        propertyDao.updatePropertyPointOfInterest(PointsOfInterest(id = 1,
            propertyId = 1,
            school = false,
            shoppingCenter = false,
            stations = false,
            sportsClubs = false,
            parks = true,
            university = true))
        pointsOfInterestOfProperty1 = propertyDao.getCurrentFullProperty(1).first()
        assertFalse(pointsOfInterestOfProperty1.pointsOfInterestList.school)
        assertFalse(pointsOfInterestOfProperty1.pointsOfInterestList.shoppingCenter)
        assertFalse(pointsOfInterestOfProperty1.pointsOfInterestList.stations)
        assertFalse(pointsOfInterestOfProperty1.pointsOfInterestList.sportsClubs)
        assertTrue(pointsOfInterestOfProperty1.pointsOfInterestList.parks)
        assertTrue(pointsOfInterestOfProperty1.pointsOfInterestList.university)
    }

    @Test
    fun getLastIdPropertyTable() = runBlocking {
        val property1 = Property(numberOfRooms = 1)
        val property2 = Property(numberOfRooms = 2)
        val property3 = Property(numberOfRooms = 3)
        propertyDao.insertProperty(property1)
        propertyDao.insertProperty(property2)
        propertyDao.insertProperty(property3)
        assertEquals(4, propertyDao.getLastIdPropertyTable().take(1).first())
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        db.close()
    }

}