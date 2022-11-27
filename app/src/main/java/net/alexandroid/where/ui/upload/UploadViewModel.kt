package net.alexandroid.where.ui.upload

import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import net.alexandroid.where.model.LatLng
import net.alexandroid.where.repo.LocationsRepo
import net.alexandroid.where.utils.PermissionUtils
import net.alexandroid.where.utils.copyUriContentToAppFiles
import net.alexandroid.where.utils.deleteIt
import net.alexandroid.where.utils.logs.logD
import net.alexandroid.where.utils.logs.logE
import net.alexandroid.where.utils.toDegreeFormat
import net.alexandroid.where.utils.unzip
import java.io.File
import java.util.concurrent.Executors
import kotlin.math.roundToInt

class UploadViewModel(
    private val locationsRepo: LocationsRepo, private val gson: Gson, private val geocoder: Geocoder
) : ViewModel() {

    private val _openFilePicker = MutableSharedFlow<Intent>()
    val openFilePicker = _openFilePicker.asSharedFlow()

    private var latTemp = ""
    private var lngTemp = ""
    private val channel = Channel<LatLng>()

    private val setOfCountries = mutableSetOf<String>()
    private val setOfCoordinates = mutableSetOf<String>()

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
                //handleSelectedZipUri(uri, context)
                listenForParsedData()
                parseRecordsJson(context)
            }
        }
    }

    private fun listenForParsedData() {
        viewModelScope.launch(Dispatchers.Default) {
            channel.receiveAsFlow().collect {
                getCountryByCoordinates(it)
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
        logD("Parsing done")
    }

    private fun handleLine(it: String) {
        val line = it.trim()
        when {
            line.contains("latitudeE7") -> {
                val start = line.indexOf("E7") + 5
                latTemp = line.substring(start, line.length - 1)
            }

            line.contains("longitudeE7") -> {
                val start = line.indexOf("E7") + 5
                lngTemp = line.substring(start, line.length - 1)
            }

            line.contains("accuracy") -> {
                val start = line.indexOf(":") + 2
                val accuracy = line.substring(start, line.length - 1)
                //logD("accuracy: $accuracy")
                handleCoordinates(latTemp, lngTemp, accuracy)
            }
        }
    }

    private fun handleCoordinates(lat: String, lng: String, accuracy: String) {
        viewModelScope.launch(Dispatchers.Default) {
            val accuracyInt = if(accuracy.isNotEmpty()) accuracy.toInt() else 100
            if (accuracyInt < 100) {
                channel.send(LatLng(lat.toDegreeFormat(), lng.toDegreeFormat(), accuracyInt))
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun getCountryByCoordinates(latLng: LatLng): String? {
        //logD("latLng: $latLng")
        //val roundLat = (latLng.lat * 10.0).roundToInt() / 10.0
        //val roundLng = (latLng.lng * 10.0).roundToInt() / 10.0
        val roundLng = latLng.lat.roundToInt()
        val roundLat = latLng.lng.roundToInt()
        val key = "${roundLat}_${roundLng}"
        //logD("key: $key")

        if (setOfCoordinates.contains(key)) {
            return null
        }
        //logD("It is a new key: $key")
        setOfCoordinates.add(key)

        try {
            val addressesList = geocoder.getFromLocation(latLng.lat, latLng.lng, 1)
            if (!addressesList.isNullOrEmpty()) {
                val countryName = addressesList[0].countryName ?: ""
                if (countryName.isNotEmpty() && !setOfCountries.contains(countryName)) {
                    logD("country: $countryName (latLng: $latLng)")
                    setOfCountries.add(countryName)
                }
                return countryName
            }
        } catch (e: Exception) {
            logE("Failed to get location based on coordinates", t = e)
        }
        return null
    }
}