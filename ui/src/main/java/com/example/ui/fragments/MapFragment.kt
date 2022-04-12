package com.example.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.ui.databinding.MapBinding
import com.example.ui.viewModels.MapViewModel
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class MapFragment : Fragment(), KoinComponent {

    private val viewModel: MapViewModel by inject()

    private var _binding: MapBinding? = null
    private val binding get() = _binding!!

    private lateinit var mapView: MapView
    private lateinit var mbMap: MapboxMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = binding.mapView

        mapView.getMapAsync {

            mbMap = it

            mbMap.setStyle(Style.MAPBOX_STREETS) {

                val locationComponent = mbMap.locationComponent

                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    val permissionsManager = PermissionsManager(object : PermissionsListener {
                        override fun onExplanationNeeded(permissionsToExplain: List<String>) {
                            Toast.makeText(activity, "location not enabled", Toast.LENGTH_LONG).show()
                        }
                        override fun onPermissionResult(granted: Boolean) {
                            if (!granted) {
                                Toast.makeText(
                                    activity,
                                    "Location services not allowed",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    })
                    permissionsManager.requestLocationPermissions(activity)
                }

                locationComponent.activateLocationComponent(requireContext(), mbMap.style!!)
                locationComponent.isLocationComponentEnabled = true
                locationComponent.cameraMode = CameraMode.TRACKING
                locationComponent.renderMode = RenderMode.COMPASS
            }
        }

    }

    @Override
    @SuppressWarnings("MissingPermission")
    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    @Override
    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    @Override
    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    @Override
    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    @Override
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    @Override
    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    @Override
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @Override
    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

}