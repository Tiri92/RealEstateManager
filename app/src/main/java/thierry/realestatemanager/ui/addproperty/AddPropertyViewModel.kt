package thierry.realestatemanager.ui.addproperty

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import thierry.realestatemanager.model.Media
import thierry.realestatemanager.model.Property
import thierry.realestatemanager.repositories.LocalDatabaseRepository
import javax.inject.Inject

@HiltViewModel
class AddPropertyViewModel @Inject constructor(private val localDatabaseRepository: LocalDatabaseRepository) :
    ViewModel() {
    var getLastIdPropertyTable = localDatabaseRepository.getLastIdPropertyTable().asLiveData()

    fun insertMedia(media: Media) =
        viewModelScope.launch { localDatabaseRepository.insertPropertyMedia(media) }

    fun insertProperty(property: Property) =
        viewModelScope.launch { localDatabaseRepository.insertProperty(property) }

    private var mutableListOfMedia = MutableLiveData<List<Media>>()
    private var listOfMedia: MutableList<Media> = mutableListOf()
    fun addMedia(media: Media) {
        listOfMedia.add(media)
        listOfMedia.sortBy { it.uri }
        mutableListOfMedia.value = listOfMedia
    }

    fun setDescriptionOfMedia(description: String, uri: String) {
        listOfMedia.find {  it.uri == uri }?.description = description
    }

    fun getListOfMedia():LiveData<List<Media>> {
        return mutableListOfMedia
    }

}