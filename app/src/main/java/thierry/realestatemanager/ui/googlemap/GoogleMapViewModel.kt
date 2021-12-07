package thierry.realestatemanager.ui.googlemap

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import thierry.realestatemanager.model.GeocodingResponse
import thierry.realestatemanager.model.Property
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

    fun callGeocoding(address: String, propertyId: Int) {
        geocodingRepository.callGeocoding(address, propertyId)
    }

    fun getGeocodingResponse(): LiveData<GeocodingResponse> {
        return geocodingRepository.getGeocodingResponse()
    }

    fun updateProperty(property: Property) =
        viewModelScope.launch { localDatabaseRepository.updateProperty(property) }

    var currentProperty: Property = Property()

    var currentPosition: LatLng? = null

}