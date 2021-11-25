package thierry.realestatemanager.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import thierry.realestatemanager.model.Photo
import thierry.realestatemanager.model.Property
import thierry.realestatemanager.model.Video
import thierry.realestatemanager.repositories.LocalDatabaseRepository
import javax.inject.Inject

@HiltViewModel
class AddPropertyViewModel @Inject constructor(private val localDatabaseRepository: LocalDatabaseRepository) :
    ViewModel() {
    var allPropertyPhoto = localDatabaseRepository.allPropertyPhoto().asLiveData()
    var getLastIdPropertyTable = localDatabaseRepository.getLastIdPropertyTable().asLiveData()
    fun insertPhoto(photo: Photo) =
        viewModelScope.launch { localDatabaseRepository.insertPropertyPhoto(photo) }
    fun insertVideo(video: Video) =
        viewModelScope.launch { localDatabaseRepository.insertPropertyVideo(video) }
    fun insertProperty(property: Property) =
        viewModelScope.launch { localDatabaseRepository.insertProperty(property) }
}