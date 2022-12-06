package net.alexandroid.where.ui.list

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.map
import net.alexandroid.where.repo.LocationsRepo

class ListViewModel(private val locationsRepo: LocationsRepo) : ViewModel() {
    fun getLocations() = locationsRepo.getLocations().map { list ->
        list.map {
            it.countryName
        }
    }
}