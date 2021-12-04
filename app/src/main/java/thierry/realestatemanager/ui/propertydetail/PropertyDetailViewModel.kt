package thierry.realestatemanager.ui.propertydetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import thierry.realestatemanager.model.Property
import thierry.realestatemanager.repositories.LocalDatabaseRepository
import thierry.realestatemanager.repositories.SharedRepository
import javax.inject.Inject

@HiltViewModel
class PropertyDetailViewModel @Inject constructor(
    private val localDatabaseRepository: LocalDatabaseRepository,
    private val sharedRepository: SharedRepository
) :
    ViewModel() {

    var currentProperty: Property = Property()

    fun getCurrentFullProperty(id: Int) =
        localDatabaseRepository.getCurrentFullProperty(id).asLiveData()

    fun setCurrentPropertyId(id: Int) {
        sharedRepository.setCurrentPropertyId(id)
    }

    fun updateProperty(property: Property) =
        viewModelScope.launch { localDatabaseRepository.updateProperty(property) }

}