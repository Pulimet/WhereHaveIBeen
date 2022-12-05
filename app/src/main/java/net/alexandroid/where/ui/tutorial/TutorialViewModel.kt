package net.alexandroid.where.ui.tutorial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import net.alexandroid.where.repo.LocationsRepo
import net.alexandroid.where.utils.logs.logD

class TutorialViewModel(private val locationsRepo: LocationsRepo) : ViewModel() {
    private val _navigateToMap = MutableSharedFlow<Unit>()
    val navigateToMap = _navigateToMap.asSharedFlow()

    fun onViewCreated() {
        logD()
        viewModelScope.launch {
            locationsRepo.getLocations().collectLatest {
                if (it.isNotEmpty()) {
                    _navigateToMap.emit(Unit)
                }
            }
        }
    }

}