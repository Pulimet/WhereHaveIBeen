package net.alexandroid.where.ui.country

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import net.alexandroid.where.R
import net.alexandroid.where.repo.SearchRepo
import net.alexandroid.where.utils.GetResource

class CountryViewModel(private val searchRepo: SearchRepo, private val getResource: GetResource) : ViewModel() {

    private val _searchResult = MutableSharedFlow<String>()
    val searchResult = _searchResult.asSharedFlow()

    fun onViewCreated(country: String) {
        viewModelScope.launch {
            val title = try {
                searchRepo.getSearchResults(country).items[0].title
            } catch (e: Exception) {
                getResource.getString(R.string.search_failed)
            }
            _searchResult.emit(title)
        }
    }
}