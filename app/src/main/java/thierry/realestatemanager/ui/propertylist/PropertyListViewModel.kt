package thierry.realestatemanager.ui.propertylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import thierry.realestatemanager.model.FullProperty
import thierry.realestatemanager.repositories.LocalDatabaseRepository
import thierry.realestatemanager.repositories.SharedRepository
import javax.inject.Inject

@HiltViewModel
class PropertyListViewModel @Inject constructor(
    localDatabaseRepository: LocalDatabaseRepository,
    private val sharedRepository: SharedRepository,
) :
    ViewModel() {

    var getFullPropertyList = localDatabaseRepository.getFullPropertyList().asLiveData()

    fun getFilteredFullPropertyList(): LiveData<MutableList<FullProperty>> =
        sharedRepository.getFilteredFullPropertyList()

    fun clearFilteredFullPropertyList() = sharedRepository.clearFilteredFullPropertyList()

}