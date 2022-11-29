package net.alexandroid.where.ui.map

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import net.alexandroid.where.R
import net.alexandroid.where.databinding.FragmentMapBinding
import net.alexandroid.where.ui.binding.FragmentBinding
import net.alexandroid.where.utils.collectIt
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapFragment : Fragment(R.layout.fragment_map), OnMapReadyCallback {

    private val binding by FragmentBinding(FragmentMapBinding::bind)
    private val viewModel: MapViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.map.getFragment<SupportMapFragment>().getMapAsync(this)
    }

    // OnMapReadyCallback
    override fun onMapReady(map: GoogleMap) {
        observeLocations(map)
    }

    private fun observeLocations(map: GoogleMap) {
        viewModel.getLocations().collectIt(viewLifecycleOwner) { list ->
            list.forEach {
                map.addMarker(
                    MarkerOptions()
                        .position(LatLng(it.lat, it.lng))
                        .title(it.countryName)
                )
            }
        }
    }
}