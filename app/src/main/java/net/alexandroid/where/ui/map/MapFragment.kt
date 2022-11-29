package net.alexandroid.where.ui.map

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import net.alexandroid.where.R
import net.alexandroid.where.databinding.FragmentMapBinding
import net.alexandroid.where.ui.binding.FragmentBinding
import net.alexandroid.where.utils.logs.logD

class MapFragment : Fragment(R.layout.fragment_map), OnMapReadyCallback {

    private val binding by FragmentBinding(FragmentMapBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.map.getFragment<SupportMapFragment>().getMapAsync(this)
    }

    // OnMapReadyCallback
    override fun onMapReady(map: GoogleMap) {
        logD()
    }
}