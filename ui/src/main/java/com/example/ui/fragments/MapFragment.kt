package com.example.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ui.databinding.MapBinding
import com.example.ui.viewModels.MapViewModel
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
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

        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS)
    }

    @Override
    @SuppressWarnings("MissingPermission")
    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    @Override
    override fun onStop() {
        super.onStop()
        mapView.onStop()
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