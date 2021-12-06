package thierry.realestatemanager.service

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import thierry.realestatemanager.BuildConfig
import thierry.realestatemanager.model.GeocodingResponse

interface GeocodingService {

    @GET("geocode/json?language=fr&key=${BuildConfig.MAPS_API_KEY}")
    fun getGPSLocationFromAddress(@Query("address") address: String): Call<GeocodingResponse>

}