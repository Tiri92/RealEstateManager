package thierry.realestatemanager.ui.googlemap

import android.location.Location
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import thierry.realestatemanager.repositories.LocalDatabaseRepository
import javax.inject.Inject

@HiltViewModel
class GoogleMapViewModel @Inject constructor(private val localDatabaseRepository: LocalDatabaseRepository) :
    ViewModel() {
    fun setLocationInLatLng(location: Location): LatLng {
        return LatLng(location.latitude, location.longitude)
    }

    var currentPosition: LatLng? = null

}