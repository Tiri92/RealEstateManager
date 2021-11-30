package thierry.realestatemanager.ui.updateproperty

import android.util.Log
import android.util.Property
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import thierry.realestatemanager.repositories.LocalDatabaseRepository
import javax.inject.Inject

@HiltViewModel
class UpdatePropertyViewModel @Inject constructor(private val localDatabaseRepository: LocalDatabaseRepository) :
    ViewModel() {
    private var actualPropertyIndex: Int = 2
    private var actualProperty = localDatabaseRepository.actualProperty(actualPropertyIndex).asLiveData()
    fun setPropertyId(id: String){
        actualPropertyIndex = id.toInt()
    Log.i("TEST", "id$id")
    }

    fun getActualProperty(): LiveData<thierry.realestatemanager.model.Property> {
        return actualProperty
    }

}