package thierry.realestatemanager

import org.junit.Assert.assertEquals
import org.junit.Test
import thierry.realestatemanager.utils.Utils

class UtilsUnitTest {

    private val todayDate = "13/12/2021" //change date before test
    private val euro = 82
    private val dollar = 100

    @Test
    fun getTodayDateFormat() {
        assertEquals(Utils.getTodayDate(), todayDate)
    }

    @Test
    fun convertDollarToEuro() {
        assertEquals(
            Utils.convertDollarToEuro(dollar), euro
        )
    }

    @Test
    fun convertEuroToDollar() {
        assertEquals(Utils.convertEuroToDollar(euro),
            dollar)
    }

}