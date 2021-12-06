package thierry.realestatemanager.ui.googlemap

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import thierry.realestatemanager.model.GeocodingResponse
import thierry.realestatemanager.repositories.GeocodingRepository
import thierry.realestatemanager.repositories.LocalDatabaseRepository
import javax.inject.Inject

@HiltViewModel
class GoogleMapViewModel @Inject constructor(
    private val localDatabaseRepository: LocalDatabaseRepository,
    private val geocodingRepository: GeocodingRepository,
) :
    ViewModel() {

    fun getFullPropertyList() = localDatabaseRepository.getFullPropertyList().asLiveData()

    fun setLocationInLatLng(location: Location): LatLng {
        return LatLng(location.latitude, location.longitude)
    }

    fun callGeocoding(address: String) {
        geocodingRepository.callGeocoding(address)
    }

    fun getListOfGeocodingResponse():LiveData<List<GeocodingResponse>> {
        return geocodingRepository.getListOfGeocodingResponse()
    }

    var currentPosition: LatLng? = null

}