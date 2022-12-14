package net.alexandroid.where.utils

import android.content.Context
import android.net.Uri
import net.alexandroid.where.utils.logs.logD
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.zip.ZipFile

object FilesUtils {
    fun handleSelectedZipUri(uri: Uri, context: Context, updateStatus: (status: String) -> Unit) {
        logD("Selected URI: $uri")
        // Downloaded file copied to: files/timeline.zip
        uri.copyUriContentToAppFiles(context).apply {
            updateStatus("File uploaded! \n")
            // Unzipped to:
            // 1. files/Takeout/Location History/Records.json
            // 2. files/Takeout/Location History/Semantic Location History/
            //    20XX/20XX_AUGUST.json
            unzip()
            updateStatus("File unzipped! \n")
            deleteIt()
            updateStatus("Uploaded Zip file deleted. \n")
        }
        logD("Unzip completed \n\n")
    }
}

fun File.unzip() {
    val destinationDir = parentFile
    logD("====> destinationDir: $destinationDir")

    ZipFile(this).use { zipFile ->
        zipFile
            .entries()
            .asSequence()
            .filter { !it.isDirectory }
            .forEach { zipEntry ->
                val currFile = File(destinationDir, zipEntry.name)
                currFile.parentFile?.mkdirs()
                zipFile.getInputStream(zipEntry).use { input ->
                    currFile.outputStream().use { output -> input.copyTo(output) }
                }
            }
    }
}

fun File.deleteIt() {
    val isCompleted = delete()
    logD("Is delete completed: $isCompleted")
}

fun Uri.copyUriContentToAppFiles(context: Context): File {
    val contentUri = this
    val selectedFile = File(context.filesDir, "timeline.zip")
    logD("SelectedFile: ${selectedFile.path}")
    val input = context.contentResolver.openInputStream(contentUri)
    val out = FileOutputStream(selectedFile)
    copyInputStreamToFile(input, out)
    return selectedFile
}

private fun copyInputStreamToFile(inputStream: InputStream?, outputStream: FileOutputStream) {
    if (inputStream == null) return
    val buffer = ByteArray(8192)
    inputStream.use { input ->
        outputStream.use { fileOut ->

            while (true) {
                val length = input.read(buffer)
                if (length <= 0)
                    break
                fileOut.write(buffer, 0, length)
            }
            fileOut.flush()
            fileOut.close()
        }
    }
    inputStream.close()
}