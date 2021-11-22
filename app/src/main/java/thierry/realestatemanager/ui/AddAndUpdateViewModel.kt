package thierry.realestatemanager.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import thierry.realestatemanager.model.Photo
import thierry.realestatemanager.repositories.LocalDatabaseRepository
import javax.inject.Inject

@HiltViewModel
class AddAndUpdateViewModel @Inject constructor(private val localDatabaseRepository: LocalDatabaseRepository) :
    ViewModel() {
    var allPropertyPhoto = localDatabaseRepository.allPropertyPhoto().asLiveData()
    fun insertPhoto(photo: Photo) =
        viewModelScope.launch { localDatabaseRepository.insertPropertyPhoto(photo) }
}