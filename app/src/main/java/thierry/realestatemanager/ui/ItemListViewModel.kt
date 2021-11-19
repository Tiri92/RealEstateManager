package thierry.realestatemanager.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import thierry.realestatemanager.repositories.LocalDatabaseRepository
import javax.inject.Inject

@HiltViewModel
class ItemListViewModel @Inject constructor(private val localDatabaseRepository: LocalDatabaseRepository) :
    ViewModel() {
    var allProperty = localDatabaseRepository.allProperty().asLiveData()
    var allPropertyPhoto = localDatabaseRepository.allPropertyPhoto().asLiveData()
}