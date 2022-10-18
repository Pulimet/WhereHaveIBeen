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
import net.alexandroid.where.utils.collectIt
import org.koin.androidx.viewmodel.ext.android.viewModel


class UploadFragment : Fragment(R.layout.fragment_upload), View.OnClickListener {
    private val binding by FragmentBinding(FragmentUploadBinding::bind)
    private val viewModel: UploadViewModel by viewModel()

    private val requestPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            onPermissionsResult(permissions)
        }

    private fun onPermissionsResult(permissions: Map<String, Boolean>) {
        viewModel.onPermissionsResult(permissions, requireActivity(), requestPermissions)
    }

    private var resultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { viewModel.onFileSelected(it, requireContext()) }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        binding.buttonSecond.setOnClickListener(this)
    }

    private fun observeViewModel() {
        viewModel.openFilePicker.collectIt(viewLifecycleOwner) {
            resultLauncher.launch(it)
        }
    }

    // View.OnClickListener
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_second -> viewModel.onButtonClick(requireActivity(), requestPermissions)
        }
    }
}

