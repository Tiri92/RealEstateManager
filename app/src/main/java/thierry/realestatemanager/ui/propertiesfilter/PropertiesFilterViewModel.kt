package thierry.realestatemanager.ui.propertiesfilter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.sqlite.db.SupportSQLiteQuery
import dagger.hilt.android.lifecycle.HiltViewModel
import thierry.realestatemanager.model.FullProperty
import thierry.realestatemanager.repositories.LocalDatabaseRepository
import thierry.realestatemanager.repositories.SharedRepository
import javax.inject.Inject

@HiltViewModel
class PropertiesFilterViewModel @Inject constructor(
    private val localDatabaseRepository: LocalDatabaseRepository,
    private val sharedRepository: SharedRepository,
) :
    ViewModel() {

    var selectedDateOfCreation: Long? = null
    var selectedDateOfSale: Long? = null
    var selectedFormattedDateOfCreation: String? = null
    var selectedFormattedDateOfSale: String? = null
    var minPrice: Int? = null
    var maxPrice: Int? = null
    var minSurface: Int? = null
    var maxSurface: Int? = null
    var minMedia: Int? = null
    var formattedPriceRange: String? = null
    var formattedSurfaceRange: String? = null
    var formattedMinMedia: String? = null

    fun getFilteredFullPropertyList(query: SupportSQLiteQuery) =
        localDatabaseRepository.getFilteredFullPropertyList(query).asLiveData()

    fun setFilteredFullPropertyList(filteredFullPropertyList: MutableList<FullProperty>) {
        sharedRepository.setFilteredFullPropertyList(filteredFullPropertyList)
    }

}