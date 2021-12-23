package thierry.realestatemanager

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows
import org.robolectric.shadows.ShadowNetworkInfo
import thierry.realestatemanager.utils.Utils

@RunWith(AndroidJUnit4::class)
class ShadowConnectivityManagerTest {

    private var connectivityManager: ConnectivityManager? = null
    private var shadowOfActiveNetworkInfo = ShadowNetworkInfo()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        connectivityManager =
            getApplicationContext<Context>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        shadowOfActiveNetworkInfo = Shadows.shadowOf(connectivityManager!!.activeNetworkInfo)
    }

    @Test
    fun getActiveNetworkInfo_shouldReturnTrueCorrectly() {
        shadowOfActiveNetworkInfo.setConnectionStatus(NetworkInfo.State.CONNECTED)
        Assert.assertTrue(Utils.isConnectedToInternet(getApplicationContext()))
    }

    @Test
    fun getActiveNetworkInfo_shouldReturnFalseCorrectly() {
        shadowOfActiveNetworkInfo.setConnectionStatus(NetworkInfo.State.DISCONNECTED)
        Assert.assertFalse(Utils.isConnectedToInternet(getApplicationContext()))
    }

}