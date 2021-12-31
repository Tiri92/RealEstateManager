package thierry.realestatemanager

import android.content.ContentResolver
import android.content.ContentUris
import androidx.room.Room
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import thierry.realestatemanager.database.PropertyDatabase
import thierry.realestatemanager.databasecontentprovider.ContentProvider

@RunWith(AndroidJUnit4::class)
class ContentProviderInstrumentedTest {

    private var myContentResolver: ContentResolver? = null

    @Before
    fun setup() {
        Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            PropertyDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        myContentResolver = InstrumentationRegistry.getInstrumentation().context.contentResolver
    }

    @Test
    fun getProperty() {
        val cursor = myContentResolver!!.query(
            ContentUris.withAppendedId(ContentProvider.URI_ITEM, 1),
            null,
            null,
            null,
            null
        )
        assertThat(cursor, Matchers.notNullValue())
        Assert.assertTrue(cursor!!.count >= 4)
        cursor.close()
    }

}