package thierry.realestatemanager.ui.propertylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import thierry.realestatemanager.repositories.LocalDatabaseRepository
import javax.inject.Inject

@HiltViewModel
class PropertyListViewModel @Inject constructor(private val localDatabaseRepository: LocalDatabaseRepository) :
    ViewModel() {
    var allProperty = localDatabaseRepository.allProperty().asLiveData()
}