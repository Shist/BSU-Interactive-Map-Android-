package com.example.ui.fragments

import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.domain.BuildingItem
import com.example.ui.R
import com.example.ui.databinding.MapBinding
import com.example.ui.viewModels.LoadState
import com.example.ui.viewModels.MapViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

class MapFragment : Fragment(), KoinComponent {

    private val viewModel: MapViewModel by inject()

    private var _binding: MapBinding? = null
    private val binding get() = _binding!!

    private lateinit var map: MapView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ctx = view.context
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))

        map = view.findViewById(R.id.map)
        map.setTileSource(TileSourceFactory.MAPNIK)

        map.setMultiTouchControls(true)

        val pointBelarus = GeoPoint(53.894,27.547)

        map.controller.setCenter(pointBelarus)
        map.controller.setZoom(12.0)

        val pointInfo = map.mapCenter.toString()
        val zoomInfo = map.zoomLevelDouble.toString()
        Log.d("point_on_map", pointInfo)
        Log.d("zoom_on_map", zoomInfo)

        var dataList: List<BuildingItem> = emptyList()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.dataFlow.collect {
                    dataList = it
                    Log.d("JSON DATA", "Json data, received by MapViewModel:\n$dataList")
                }
            }
        }

        val timeLength = if (dataList.isEmpty()) // Если данных вообще нету (даже в базе)
            Snackbar.LENGTH_INDEFINITE
        else // Если новые данные не пришли, но есть старые данные в базе
            Snackbar.LENGTH_LONG

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect {
                    when (it) {
                        LoadState.LOADING -> {
                            createSnackbar(resources.getString(R.string.loading),
                                requireContext().getColor(R.color.black))
                        }
                        LoadState.SUCCESS -> {
                            createSnackbar(resources.getString(R.string.loadingSuccess),
                                requireContext().getColor(R.color.black))
                        }
                        LoadState.INTERNET_ERROR -> {
                            createSnackbarWithReload(timeLength,
                                resources.getString(R.string.errorNetwork))
                        }
                        LoadState.UNKNOWN_ERROR -> {
                            createSnackbarWithReload(timeLength,
                                resources.getString(R.string.errorUnknownNoData))
                        }
                        LoadState.EMPTY_DATA_ERROR -> {
                            createSnackbarWithReload(timeLength,
                                resources.getString(R.string.errorNoNewsOnAPI))
                        }
                        else -> createSnackbar(resources.getString(R.string.launching),
                            requireContext().getColor(R.color.black))
                    }
                }
            }
        }

        if (savedInstanceState == null && viewModel.state.value == LoadState.IDLE) {
            viewModel.loadData()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createSnackbar(message: String, color: Int) {
        val snackbar = Snackbar.make(
            binding.mapContainer,
            message,
            BaseTransientBottomBar.LENGTH_SHORT
        )
        snackbar.setTextColor(color)
        snackbar.show()
    }

    private fun createSnackbarWithReload(snackbarTimeLength: Int, messageError: String) {
        val snackbar = Snackbar.make(
            binding.mapContainer,
            messageError,
            snackbarTimeLength
        )
        snackbar.setTextColor(requireContext().getColor(R.color.black))
        snackbar.setActionTextColor(requireContext().getColor(R.color.black))
        snackbar.setAction(R.string.reload) {
            viewModel.loadData()
        }
        snackbar.show()
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

}