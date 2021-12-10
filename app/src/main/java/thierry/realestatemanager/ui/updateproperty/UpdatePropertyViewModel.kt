package thierry.realestatemanager.ui.updateproperty

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import thierry.realestatemanager.model.Media
import thierry.realestatemanager.model.PointsOfInterest
import thierry.realestatemanager.model.Property
import thierry.realestatemanager.repositories.LocalDatabaseRepository
import thierry.realestatemanager.repositories.SharedRepository
import javax.inject.Inject

@HiltViewModel
class UpdatePropertyViewModel @Inject constructor(
    private val localDatabaseRepository: LocalDatabaseRepository,
    private val sharedRepository: SharedRepository,
) :
    ViewModel() {

    fun getCurrentFullProperty() =
        localDatabaseRepository.getCurrentFullProperty(sharedRepository.getCurrentPropertyId())
            .asLiveData()

    var publicListOfMedia: MutableList<Media> = mutableListOf()
    private var mutableListOfMedia = MutableLiveData<List<Media>>()
    private var listOfMedia: MutableList<Media> = mutableListOf()

    fun getMutableListOfMedia(): MutableList<Media> {
        return listOfMedia
    }

    fun initListOfMedia() {
        listOfMedia.addAll(publicListOfMedia)
        mutableListOfMedia.value = listOfMedia
    }

    fun addMedia(media: Media) {
        listOfMedia.add(media)
        listOfMedia.sortBy { it.uri }
        mutableListOfMedia.value = listOfMedia
    }

    fun deleteMedia(media: Media) {
        listOfMedia.remove(media)
        mutableListOfMedia.value = listOfMedia
    }

    var mediaListToSet: MutableList<Media> = mutableListOf()
    fun setDescriptionOfMedia(description: String, uri: String) {
        for (media in listOfMedia) {
            if (media.uri == uri) {
                media.description = description
            }
            if (!mediaListToSet.contains(media)) {
                mediaListToSet.add(media)
            }
        }
    }

    fun getListOfMedia(): LiveData<List<Media>> {
        return mutableListOfMedia
    }

    fun insertPropertyMedia(media: Media) =
        viewModelScope.launch { localDatabaseRepository.insertPropertyMedia(media) }

    fun updateProperty(property: Property) =
        viewModelScope.launch { localDatabaseRepository.updateProperty(property) }

    fun updatePropertyPointOfInterest(pointOfInterest: PointsOfInterest) =
        viewModelScope.launch {
            localDatabaseRepository.updatePropertyPointOfInterest(pointOfInterest)
        }

    var listOfMediaToDelete: MutableList<Media> = mutableListOf()

    fun deletePropertyMediaFromDb(media: Media) =
        viewModelScope.launch { localDatabaseRepository.deletePropertyMedia(media) }

}