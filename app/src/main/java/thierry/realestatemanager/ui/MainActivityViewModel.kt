package thierry.realestatemanager.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import thierry.realestatemanager.repositories.LocalDatabaseRepository
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(localDatabaseRepository: LocalDatabaseRepository) :
    ViewModel() {

    var getFullPropertyList = localDatabaseRepository.getFullPropertyList().asLiveData()

}


