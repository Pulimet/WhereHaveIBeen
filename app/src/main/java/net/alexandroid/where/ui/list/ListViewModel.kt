package net.alexandroid.where.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import net.alexandroid.where.repo.LocationsRepo

class ListViewModel(private val locationsRepo: LocationsRepo) : ViewModel() {

    private val _navigateToDetails = MutableSharedFlow<String>()
    val navigateToDetails = _navigateToDetails.asSharedFlow()

    fun getLocations() = locationsRepo.getLocations().map { list ->
        list.map {
            it.countryName
        }
    }

    fun onCountryClick(country: String) {
        viewModelScope.launch {
            _navigateToDetails.emit(country)
        }
    }
}