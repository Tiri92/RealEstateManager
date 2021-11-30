package thierry.realestatemanager.repositories

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedRepository @Inject constructor() {

    private var currentPropertyId: Int = 0

    fun setCurrentPropertyId(id: Int) {
        currentPropertyId = id
        Log.i("[DETAIL]", "from repo : " + currentPropertyId)
    }

    fun getCurrentPropertyId(): Int {
        return currentPropertyId
    }

}