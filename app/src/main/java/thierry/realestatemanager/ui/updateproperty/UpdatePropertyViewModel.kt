package thierry.realestatemanager.ui.updateproperty

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import thierry.realestatemanager.model.Media
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

    fun setDescriptionOfMedia(description: String, uri: String) {
        listOfMedia.find { it.uri == uri }?.description = description
    }

    fun getListOfMedia(): LiveData<List<Media>> {
        return mutableListOfMedia
    }

}