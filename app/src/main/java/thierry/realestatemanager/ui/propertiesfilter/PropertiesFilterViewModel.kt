package thierry.realestatemanager.ui.propertiesfilter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.sqlite.db.SupportSQLiteQuery
import dagger.hilt.android.lifecycle.HiltViewModel
import thierry.realestatemanager.repositories.LocalDatabaseRepository
import javax.inject.Inject

@HiltViewModel
class PropertiesFilterViewModel @Inject constructor(private val localDatabaseRepository: LocalDatabaseRepository) :
    ViewModel() {

    fun getFilteredFullPropertyList(query: SupportSQLiteQuery) =
        localDatabaseRepository.getFilteredFullPropertyList(query).asLiveData()

}