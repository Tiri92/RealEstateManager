package thierry.realestatemanager.ui.updateproperty

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import thierry.realestatemanager.repositories.LocalDatabaseRepository
import thierry.realestatemanager.repositories.SharedRepository
import javax.inject.Inject

@HiltViewModel
class UpdatePropertyViewModel @Inject constructor(
    private val localDatabaseRepository: LocalDatabaseRepository,
    private val sharedRepository: SharedRepository
) :
    ViewModel() {

    fun getCurrentFullProperty() =
        localDatabaseRepository.getCurrentFullProperty(sharedRepository.getCurrentPropertyId())
            .asLiveData()

}