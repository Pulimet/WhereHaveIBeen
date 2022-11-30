package net.alexandroid.where.ui.map

import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import net.alexandroid.where.repo.LocationsRepo
import net.alexandroid.where.utils.PermissionUtils
import net.alexandroid.where.utils.emitSharedFlow

class MapViewModel(private val locationsRepo: LocationsRepo) : ViewModel() {
    fun getLocations() = locationsRepo.getLocations()

    private val _showCurrentLocation = MutableSharedFlow<Unit>()
    val showCurrentLocation = _showCurrentLocation.asSharedFlow()

    fun onPermissionsResult(
        permissions: Map<String, Boolean>,
        activity: FragmentActivity,
        requestPermissions: ActivityResultLauncher<Array<String>>
    ) {
        PermissionUtils.printLog(permissions)
        checkPermissionsFlow(activity, requestPermissions)
    }

    private fun checkPermissionsFlow(
        activity: FragmentActivity, requestPermissions: ActivityResultLauncher<Array<String>>
    ) {
        PermissionUtils.checkPermissionFlow(
            activity,
            requestPermissions,
            PermissionUtils.LOCATION
        ) {
            emitSharedFlow(_showCurrentLocation)
        }
    }

    fun onMapReady(
        activity: FragmentActivity, requestPermissions: ActivityResultLauncher<Array<String>>
    ) {
        checkPermissionsFlow(activity, requestPermissions)
    }
}