package thierry.realestatemanager

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import thierry.realestatemanager.utils.Utils

@RunWith(AndroidJUnit4::class)
class ConnectionInstrumentedTest {

    private lateinit var context: Context

    @Before
    fun setUpContext() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun isConnectedToInternet() {
        Assert.assertTrue(Utils.isConnectedToInternet(context))
    }

}