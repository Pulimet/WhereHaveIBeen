package net.alexandroid.where.ui.upload

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import net.alexandroid.where.R
import net.alexandroid.where.databinding.FragmentUploadBinding
import net.alexandroid.where.ui.binding.FragmentBinding

class UploadFragment : Fragment(R.layout.fragment_upload) {
    private val binding by FragmentBinding(FragmentUploadBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(UploadFragmentDirections.actionUploadFragmentToTutorialFragment())
        }
    }
}