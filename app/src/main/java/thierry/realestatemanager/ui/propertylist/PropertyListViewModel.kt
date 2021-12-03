package thierry.realestatemanager.ui.propertylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import thierry.realestatemanager.repositories.LocalDatabaseRepository
import javax.inject.Inject

@HiltViewModel
class PropertyListViewModel @Inject constructor(localDatabaseRepository: LocalDatabaseRepository) :
    ViewModel() {

    var getFullPropertyList = localDatabaseRepository.getFullPropertyList().asLiveData()

}