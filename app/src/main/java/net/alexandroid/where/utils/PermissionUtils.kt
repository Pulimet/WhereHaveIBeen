package net.alexandroid.where.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import net.alexandroid.where.R
import net.alexandroid.where.utils.logs.logD
import net.alexandroid.where.utils.logs.logW

object PermissionUtils {

    const val STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE

    fun checkPermissionFlow(
        activity: Activity,
        requestPermissions: ActivityResultLauncher<Array<String>>,
        permission: String,
        permissionGranted: () -> Unit
    ) {
        logW("")
        when {
            isPermissionGranted(activity, permission) -> permissionGranted()
            activity.shouldShowRequestPermissionRationale(permission) ->
                showDialogWithPermissionRationale(activity, requestPermissions, permission)

            else -> requestPermissions.launch(arrayOf(permission))
        }
    }

    private fun isPermissionGranted(context: Context, permission: String) =
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

    private fun showDialogWithPermissionRationale(
        activity: Activity,
        requestPermissions: ActivityResultLauncher<Array<String>>,
        permission: String
    ) {
        logD()
        AlertDialog.Builder(activity).apply {
            setMessage(getRationaleString(activity, permission))
            setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            setPositiveButton(android.R.string.ok) { _, _ ->
                logD("On dialog OK: request permission!")
                requestPermissions.launch(arrayOf(permission))
            }
            create().show()
        }
    }

    private fun getRationaleString(activity: Activity, permission: String): String =
        when (permission) {
            STORAGE -> activity.getString(R.string.storage_rationale)
            else -> ""
        }


    fun printLog(permissions: Map<String, @JvmSuppressWildcards Boolean>) {
        logD("permissions.size: ${permissions.size}")
        permissions.forEach {
            logD("${it.key} : ${it.value}")
        }
    }

}