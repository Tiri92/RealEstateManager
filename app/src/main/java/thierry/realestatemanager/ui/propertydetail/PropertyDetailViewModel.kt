package thierry.realestatemanager.ui.propertydetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import thierry.realestatemanager.model.Property
import thierry.realestatemanager.repositories.SharedRepository
import thierry.realestatemanager.repositories.LocalDatabaseRepository
import javax.inject.Inject

@HiltViewModel
class PropertyDetailViewModel @Inject constructor(
    private val localDatabaseRepository: LocalDatabaseRepository,
    private val sharedRepository: SharedRepository
) :
    ViewModel() {
    var allPropertyPhoto = localDatabaseRepository.allPropertyMedia().asLiveData()

    fun setCurrentPropertyId(id: Int) {
        sharedRepository.setCurrentPropertyId(id)
        Log.i("TEST", "id$id")
    }

    fun updateProperty(property: Property) =
        viewModelScope.launch { localDatabaseRepository.updateProperty(property) }

    var currentProperty: Property = Property()

}