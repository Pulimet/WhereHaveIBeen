package net.alexandroid.where.ui.upload

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import net.alexandroid.where.R
import net.alexandroid.where.databinding.FragmentUploadBinding
import net.alexandroid.where.ui.binding.FragmentBinding
import net.alexandroid.where.utils.PermissionUtils
import net.alexandroid.where.utils.logs.logD


class UploadFragment : Fragment(R.layout.fragment_upload) {
    private val binding by FragmentBinding(FragmentUploadBinding::bind)

    private val requestPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            PermissionUtils.printLog(permissions)
            checkStoragePermissions()
        }

    private var resultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.data == null) return@registerForActivityResult
        val uri = it.data!!.data
        val path = uri?.path
        logD("Selected URI: $uri")
        logD("Selected URI path: $uri")
    }


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
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/zip"
        resultLauncher.launch(intent)
    }

}

