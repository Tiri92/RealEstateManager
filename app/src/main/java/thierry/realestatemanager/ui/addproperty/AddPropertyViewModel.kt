package thierry.realestatemanager.ui.addproperty

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import thierry.realestatemanager.model.Media
import thierry.realestatemanager.model.Property
import thierry.realestatemanager.repositories.LocalDatabaseRepository
import javax.inject.Inject

@HiltViewModel
class AddPropertyViewModel @Inject constructor(private val localDatabaseRepository: LocalDatabaseRepository) :
    ViewModel() {
    var allPropertyMedia = localDatabaseRepository.allPropertyMedia().asLiveData()
    var getLastIdPropertyTable = localDatabaseRepository.getLastIdPropertyTable().asLiveData()
    fun insertMedia(media: Media) =
        viewModelScope.launch { localDatabaseRepository.insertPropertyMedia(media) }
    fun insertProperty(property: Property) =
        viewModelScope.launch { localDatabaseRepository.insertProperty(property) }
}