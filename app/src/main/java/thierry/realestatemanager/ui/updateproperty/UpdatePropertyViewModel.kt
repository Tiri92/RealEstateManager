package thierry.realestatemanager.ui.updateproperty

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import thierry.realestatemanager.repositories.SharedRepository
import thierry.realestatemanager.repositories.LocalDatabaseRepository
import javax.inject.Inject

@HiltViewModel
class UpdatePropertyViewModel @Inject constructor(
    private val localDatabaseRepository: LocalDatabaseRepository,
    private val sharedRepository: SharedRepository
) :
    ViewModel() {

    private var currentProperty =
        localDatabaseRepository.getCurrentProperty(sharedRepository.getCurrentPropertyId())
            .asLiveData()


    fun getCurrentProperty(): LiveData<thierry.realestatemanager.model.Property> {
        return currentProperty
    }

    var allPropertyMedia = localDatabaseRepository.allPropertyMedia().asLiveData()

}