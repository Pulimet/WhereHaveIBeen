package net.alexandroid.where.ui.map

import androidx.lifecycle.ViewModel
import net.alexandroid.where.repo.LocationsRepo

class MapViewModel(private val locationsRepo: LocationsRepo): ViewModel() {
    fun getLocations() = locationsRepo.getLocations()
}