package net.alexandroid.where.ui.map

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import net.alexandroid.where.R
import net.alexandroid.where.databinding.FragmentMapBinding
import net.alexandroid.where.ui.binding.FragmentBinding

class MapFragment : Fragment(R.layout.fragment_map) {

    private val binding by FragmentBinding(FragmentMapBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(MapFragmentDirections.toListFragment())
        }
    }
}