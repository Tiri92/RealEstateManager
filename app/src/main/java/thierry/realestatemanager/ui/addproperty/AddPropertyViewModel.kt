package thierry.realestatemanager.ui.addproperty

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import thierry.realestatemanager.model.Media
import thierry.realestatemanager.model.PointsOfInterest
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
        viewModelScope.launch {
            val mutableIsSuccessfullyInserted = localDatabaseRepository.insertProperty(property)
            isSuccessfullyInserted.value = mutableIsSuccessfullyInserted
        }

    fun insertPointsOfInterest(pointsOfInterest: PointsOfInterest) =
        viewModelScope.launch { localDatabaseRepository.insertPointsOfInterest(pointsOfInterest) }

    private var mutableListOfMedia = MutableLiveData<List<Media>>()
    private var listOfMedia: MutableList<Media> = mutableListOf()
    fun addMedia(media: Media) {
        listOfMedia.add(media)
        listOfMedia.sortBy { it.uri }
        mutableListOfMedia.value = listOfMedia
    }

    fun deleteMedia(media: Media) {
        listOfMedia.remove(media)
        mutableListOfMedia.value = listOfMedia
    }

    fun setDescriptionOfMedia(description: String, uri: String) {
        listOfMedia.find { it.uri == uri }?.description = description
    }

    fun getListOfMedia(): LiveData<List<Media>> {
        return mutableListOfMedia
    }

    private var isSuccessfullyInserted = MutableLiveData<Long>()
    fun propertySuccessfullyInserted(): LiveData<Long> {
        return isSuccessfullyInserted
    }

}