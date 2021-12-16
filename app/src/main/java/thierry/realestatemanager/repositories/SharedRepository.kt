package thierry.realestatemanager.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import thierry.realestatemanager.model.FullProperty
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedRepository @Inject constructor() {

    private var currentPropertyId: Int = 0
    private var filteredFullPropertyList: MutableLiveData<MutableList<FullProperty>> =
        MutableLiveData()

    fun setCurrentPropertyId(id: Int) {
        currentPropertyId = id
    }

    fun getCurrentPropertyId(): Int {
        return currentPropertyId
    }

    fun setFilteredFullPropertyList(filteredFullPropertyList: MutableList<FullProperty>) {
        this.filteredFullPropertyList.value = filteredFullPropertyList
    }

    fun getFilteredFullPropertyList(): LiveData<MutableList<FullProperty>> {
        return filteredFullPropertyList
    }

    fun clearFilteredFullPropertyList() {
        filteredFullPropertyList.value!!.clear()
    }

}