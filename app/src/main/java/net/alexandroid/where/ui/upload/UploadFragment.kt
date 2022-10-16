package net.alexandroid.where.ui.upload

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.alexandroid.where.R
import net.alexandroid.where.databinding.FragmentUploadBinding
import net.alexandroid.where.ui.binding.FragmentBinding
import net.alexandroid.where.utils.PermissionUtils
import net.alexandroid.where.utils.copyUriContentToAppFiles
import net.alexandroid.where.utils.logs.logD
import net.alexandroid.where.utils.unzip


class UploadFragment : Fragment(R.layout.fragment_upload) {
    private val binding by FragmentBinding(FragmentUploadBinding::bind)

    private val requestPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            PermissionUtils.printLog(permissions)
            checkStoragePermissions()
        }

    private var resultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { onFileSelected(it) }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSecond.setOnClickListener {
            //findNavController().navigate(UploadFragmentDirections.toMapFrament())
            checkStoragePermissions()
        }
    }

    private fun checkStoragePermissions() {
        PermissionUtils.checkPermissionFlow(
            requireActivity(),
            requestPermissions,
            PermissionUtils.STORAGE
        ) {
            getZipFile()
        }
    }

    private fun getZipFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*"
        }
        resultLauncher.launch(intent)
    }

    private fun onFileSelected(it: ActivityResult) {
        lifecycleScope.launch(Dispatchers.IO) {
            it.data?.data?.let { uri ->
                copyAndUnzip(uri)
                // Downloaded file copied here: files/timeline.zip
                // Unzipped:
                // 1. files/Takeout/Location History/Records.json
                // 2. files/Takeout/Location History/
                //      Semantic Location History/
                //      20XX/
                //      20XX_AUGUST.json
            }
        }
    }

    private fun copyAndUnzip(uri: Uri) {
        logD("Selected URI: $uri")
        uri.copyUriContentToAppFiles(requireActivity())
            .unzip()
        logD("Unzip completed")
    }

}

