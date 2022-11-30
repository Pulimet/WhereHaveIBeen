package net.alexandroid.where.ui.upload

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
        binding.btnUpload.setOnClickListener(this)
    }

    @SuppressLint("SetTextI18n")
    private fun observeViewModel() {
        viewModel.apply {
            openFilePicker.collectIt(viewLifecycleOwner) { resultLauncher.launch(it) }
            isLoading.collectIt(viewLifecycleOwner) { isLoading ->
                binding.btnUpload.isEnabled = !isLoading
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
            navigateToMap.collectIt(viewLifecycleOwner) {
                findNavController().navigate(UploadFragmentDirections.toMapFrament())
            }
            statusMessage.collectIt(viewLifecycleOwner) {
                binding.tvResults.text = "${binding.tvResults.text}$it"
            }
        }
    }

    // View.OnClickListener
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnUpload -> viewModel.onButtonClick(requireActivity(), requestPermissions)
        }
    }
}

