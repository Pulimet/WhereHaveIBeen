package net.alexandroid.where.ui.upload

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import net.alexandroid.where.utils.PermissionUtils
import net.alexandroid.where.utils.copyUriContentToAppFiles
import net.alexandroid.where.utils.deleteIt
import net.alexandroid.where.utils.logs.logD
import net.alexandroid.where.utils.unzip
import java.io.File

class UploadViewModel : ViewModel() {

    private val _openFilePicker = MutableSharedFlow<Intent>()
    val openFilePicker = _openFilePicker.asSharedFlow()

    private var location = ""
    private var isReading = false

    fun onButtonClick(
        activity: FragmentActivity,
        requestPermissions: ActivityResultLauncher<Array<String>>
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
        activity: FragmentActivity,
        requestPermissions: ActivityResultLauncher<Array<String>>
    ) {
        PermissionUtils.checkPermissionFlow(
            activity,
            requestPermissions,
            PermissionUtils.STORAGE
        ) { openFilePicker() }
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*"
        }
        _openFilePicker.tryEmit(intent)
    }

    fun onFileSelected(it: ActivityResult, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            it.data?.data?.let { uri ->
                // TODO handleSelectedZipUri(uri, context)
                parseRecordsJson(context)
            }
        }
    }

    private fun handleSelectedZipUri(uri: Uri, context: Context) {
        logD("Selected URI: $uri")
        // Downloaded file copied to: files/timeline.zip
        uri.copyUriContentToAppFiles(context).apply {
            // Unzipped to:
            // 1. files/Takeout/Location History/Records.json
            // 2. files/Takeout/Location History/Semantic Location History/
            //    20XX/20XX_AUGUST.json
            unzip()
            deleteIt()
        }
        logD("Unzip completed")
    }

    private fun parseRecordsJson(context: Context) {
        // unZippedFilesFolder: "files"
        // files/Takeout/Location History/Records.json
        val selectedFile = File(context.filesDir, "Takeout/Location History/Records.json")
        logD("SelectedFile: ${selectedFile.path}")
        val inputStream = selectedFile.inputStream()

        inputStream.bufferedReader().use { reader ->
            reader.forEachLine {
                handleLine(it)
            }
        }
    }

    private fun handleLine(it: String) {
        when {
            it.contains("latitudeE7") -> {
                isReading = true
                location += "{$it"
            }

            it.contains("timestamp") -> {
                isReading = false
                location += "$it}"
                logD(location)
                addLocationToDb(location)
                location = ""
            }

            isReading -> location += it
        }
    }

    private fun addLocationToDb(location: String) {
        // TODO
    }
}