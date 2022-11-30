package net.alexandroid.where.ui.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import net.alexandroid.where.R
import net.alexandroid.where.databinding.FragmentListBinding
import net.alexandroid.where.ui.binding.FragmentBinding

class ListFragment : Fragment(R.layout.fragment_list) {

    private val binding by FragmentBinding(FragmentListBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNext.setOnClickListener {
            //findNavController().navigate(TutorialFragmentDirections.actionTutorialFragmentToUploadFragment())
        }
    }
}