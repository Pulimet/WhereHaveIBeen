package net.alexandroid.where.ui.tutorial

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import net.alexandroid.where.R
import net.alexandroid.where.databinding.FragmentTutorialBinding
import net.alexandroid.where.ui.binding.FragmentBinding

class TutorialFragment : Fragment(R.layout.fragment_tutorial) {

    private val binding by FragmentBinding(FragmentTutorialBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_TutorialFragment_to_UploadFragment)
        }
    }
}