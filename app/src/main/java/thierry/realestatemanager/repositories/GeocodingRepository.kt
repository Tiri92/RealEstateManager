package thierry.realestatemanager.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import thierry.realestatemanager.model.GeocodingResponse
import thierry.realestatemanager.service.GeocodingService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeocodingRepository @Inject constructor(private val geocodingService: GeocodingService) {

    private var mutableGeocodingResponse = GeocodingResponse()
    private var geocodingResponse = MutableLiveData<GeocodingResponse>()

    fun getGeocodingResponse(): LiveData<GeocodingResponse> {
        return geocodingResponse
    }

    fun callGeocoding(address: String, propertyId: Int) {
        geocodingService.getGPSLocationFromAddress(address).enqueue(object :
            Callback<GeocodingResponse> {
            override fun onResponse(
                call: Call<GeocodingResponse>,
                response: Response<GeocodingResponse>,
            ) {
                if (response.isSuccessful) {
                    response.body()!!.propertyId = propertyId
                    mutableGeocodingResponse = (response.body()!!)
                    geocodingResponse.value = mutableGeocodingResponse
                }
            }

            override fun onFailure(call: Call<GeocodingResponse>, t: Throwable) {
                Log.i("[API]", "FAIL")
            }
        })
    }

}