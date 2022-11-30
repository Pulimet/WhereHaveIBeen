package net.alexandroid.where.ui.upload

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import net.alexandroid.where.model.LatLng
import net.alexandroid.where.repo.LocationsRepo
import net.alexandroid.where.utils.FilesUtils
import net.alexandroid.where.utils.LocationUtils
import net.alexandroid.where.utils.ParsingUtils
import net.alexandroid.where.utils.PermissionUtils
import net.alexandroid.where.utils.logs.logD
import java.io.File

class UploadViewModel(
    private val locationUtils: LocationUtils,
    private val locationsRepo: LocationsRepo
) : ViewModel() {

    private val _openFilePicker = MutableSharedFlow<Intent>()
    val openFilePicker = _openFilePicker.asSharedFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _navigateToMap = MutableSharedFlow<Unit>()
    val navigateToMap = _navigateToMap.asSharedFlow()

    private val channel = Channel<LatLng>()

    private var counterTotalLocations = 0
    private var counterAccurateLocations = 0

    fun onButtonClick(
        activity: FragmentActivity, requestPermissions: ActivityResultLauncher<Array<String>>
    ) {
        /*viewModelScope.launch {
            delay(1000)
            _navigateToMap.emit(Unit)
        }*/
        checkPermissionsFlow(activity, requestPermissions)
    }

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
            PermissionUtils.STORAGE
        ) { openFilePicker() }
    }

    private fun openFilePicker() {
        logD()
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*"
        }
        viewModelScope.launch {
            _openFilePicker.emit(intent)
        }
    }

    fun onFileSelected(it: ActivityResult, context: Context) {
        logD()
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.emit(true)
            it.data?.data?.let { uri ->
                // TODO FilesUtils.handleSelectedZipUri(uri, context)
                listenForParsedData()
                parseRecordsJson(context)
            }
        }
    }

    private fun parseRecordsJson(context: Context) {
        // unZippedFilesFolder: "files"
        // files/Takeout/Location History/Records.json
        val selectedFile = File(context.filesDir, "Takeout/Location History/Records.json")
        logD("SelectedFile: ${selectedFile.path}")
        val inputStream = selectedFile.inputStream()

        inputStream.bufferedReader().use { reader ->
            reader.forEachLine { line ->
                ParsingUtils.handleLine(line)?.let { latLng ->
                    handleCoordinates(latLng)
                }
            }
        }
        onParsingDone()
    }

    private fun onParsingDone() {
        logD("Parsing done")
        // TODO Delete all files and folder Takeout
        logD("counterTotalLocations: $counterTotalLocations")
        logD("counterAccurateLocations: $counterAccurateLocations")
        logD("Total countries: ${locationUtils.countries.size}")
        logD("Countries: ${locationUtils.countries}")
        viewModelScope.launch {
            _isLoading.emit(false)
            _navigateToMap.emit(Unit)
        }
    }

    private fun handleCoordinates(latLng: LatLng) {
        counterTotalLocations++
        if (latLng.accuracy < 1000 && !latLng.src.equals("WIFI", ignoreCase = true)) {
            viewModelScope.launch(Dispatchers.Default) {
                counterAccurateLocations++
                channel.send(latLng)
            }
        }
    }

    private fun listenForParsedData() {
        viewModelScope.launch(Dispatchers.Default) {
            channel.receiveAsFlow().collect {
                handleLatLng(it)
            }
        }
    }

    private fun CoroutineScope.handleLatLng(latLng: LatLng) {
        locationUtils.getCountryByCoordinates(latLng)?.let {
            launch(Dispatchers.IO) { locationsRepo.add(it) }
        }
    }
}