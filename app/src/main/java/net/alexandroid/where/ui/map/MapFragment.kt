package net.alexandroid.where.ui.map

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
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
    private var map: GoogleMap? = null

    private val requestPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            onPermissionsResult(permissions)
        }

    private fun onPermissionsResult(permissions: Map<String, Boolean>) {
        viewModel.onPermissionsResult(permissions, requireActivity(), requestPermissions)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.map.getFragment<SupportMapFragment>().getMapAsync(this)
        viewModel.showCurrentLocation.collectIt(viewLifecycleOwner) { showCurrentLocation() }
    }

    @SuppressLint("MissingPermission")
    private fun showCurrentLocation() {
        map?.isMyLocationEnabled = true
        moveCameraToCurrentLocation()
    }

    @SuppressLint("MissingPermission")
    private fun moveCameraToCurrentLocation() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        val locationResult = fusedLocationProviderClient.lastLocation
        locationResult.addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful && task.result != null) {
                val latLng = LatLng(task.result?.latitude ?: 0.0, task.result?.longitude ?: 0.0)
                map?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 1f))
            }
        }
    }

    // OnMapReadyCallback
    override fun onMapReady(map: GoogleMap) {
        this.map = map
        observeLocations()
        viewModel.onMapReady(requireActivity(), requestPermissions)
    }

    private fun observeLocations() {
        viewModel.getLocations().collectIt(viewLifecycleOwner) { list ->
            list.forEach {
                map?.addMarker(
                    MarkerOptions()
                        .position(LatLng(it.lat, it.lng))
                        .title(it.countryName)
                )
            }
        }
    }
}