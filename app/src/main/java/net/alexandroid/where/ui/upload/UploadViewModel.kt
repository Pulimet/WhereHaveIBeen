package net.alexandroid.where.ui.upload

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import net.alexandroid.where.model.LatLng
import net.alexandroid.where.utils.FilesUtils
import net.alexandroid.where.utils.LocationUtils
import net.alexandroid.where.utils.ParsingUtils
import net.alexandroid.where.utils.PermissionUtils
import net.alexandroid.where.utils.logs.logD
import java.io.File
import java.util.concurrent.Executors

class UploadViewModel(private val locationUtils: LocationUtils) : ViewModel() {

    private val _openFilePicker = MutableSharedFlow<Intent>()
    val openFilePicker = _openFilePicker.asSharedFlow()
    private val channel = Channel<LatLng>()

    private val singleThreadDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    fun onButtonClick(
        activity: FragmentActivity, requestPermissions: ActivityResultLauncher<Array<String>>
    ) {
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
            activity, requestPermissions, PermissionUtils.STORAGE
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
        viewModelScope.launch(singleThreadDispatcher) {
            it.data?.data?.let { uri ->
                FilesUtils.handleSelectedZipUri(uri, context)
                listenForParsedData()
                parseRecordsJson(context)
            }
        }
    }

    private fun listenForParsedData() {
        viewModelScope.launch(Dispatchers.Default) {
            channel.receiveAsFlow().collect {
                locationUtils.getCountryByCoordinates(it)
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
        logD("Parsing done")
    }

    private fun handleCoordinates(latLng: LatLng) {
        viewModelScope.launch(Dispatchers.Default) {
            if (latLng.accuracy < 100) {
                channel.send(latLng)
            }
        }
    }
}