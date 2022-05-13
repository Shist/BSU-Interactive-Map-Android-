package com.example.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ui.databinding.MapBinding
import com.example.ui.viewModels.MapViewModel
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.locationcomponent.location
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import androidx.appcompat.content.res.AppCompatResources
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isGone
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.domain.BuildingItem
import com.example.ui.R
import com.example.ui.adapter.DepartmentsListAdapter
import com.example.ui.databinding.HistIconInfoBinding
import com.example.ui.databinding.ModernIconInfoBinding
import com.example.ui.mapbox.LocationPermissionHelper
import com.example.ui.viewModels.LoadState
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.ViewAnnotationAnchor
import com.mapbox.maps.extension.style.expressions.dsl.generated.color
import com.mapbox.maps.extension.style.expressions.dsl.generated.interpolate
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.annotation.AnnotationPlugin
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.viewannotation.ViewAnnotationManager
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference


class MapFragment : Fragment(), KoinComponent {

    private val viewModel: MapViewModel by inject()

    private var _binding: MapBinding? = null
    private val binding get() = _binding!!

    private var _modernIconBinding: ModernIconInfoBinding? = null
    private val modernIconBinding get() = _modernIconBinding!!

    private var _histIconBinding: HistIconInfoBinding? = null
    private val histIconBinding get() = _histIconBinding!!

    private lateinit var mapView: MapView

    private lateinit var locationPermissionHelper: LocationPermissionHelper

    private val onIndicatorBearingChangedListener = OnIndicatorBearingChangedListener {
        mapView.getMapboxMap().setCamera(CameraOptions.Builder().bearing(it).build())
    }

    private val onIndicatorPositionChangedListener = OnIndicatorPositionChangedListener {
        mapView.getMapboxMap().setCamera(CameraOptions.Builder().center(it).build())
        mapView.gestures.focalPoint = mapView.getMapboxMap().pixelForCoordinate(it)
    }

    private val onMoveListener = object : OnMoveListener {
        override fun onMoveBegin(detector: MoveGestureDetector) {
            onCameraTrackingDismissed()
        }

        override fun onMove(detector: MoveGestureDetector): Boolean {
            return false
        }

        override fun onMoveEnd(detector: MoveGestureDetector) {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MapBinding.inflate(inflater, container, false)
        _modernIconBinding = ModernIconInfoBinding.inflate(inflater, container, false)
        _histIconBinding = HistIconInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView = binding.mapView

        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS)

        mapView.getMapboxMap().loadStyleUri(
            Style.MAPBOX_STREETS
        ) { // After the style is loaded, initialize the Location component.
            mapView.location.updateSettings {
                enabled = true
                pulsingEnabled = true
            }
        }

        locationPermissionHelper = LocationPermissionHelper(WeakReference(requireActivity()))
        locationPermissionHelper.checkPermissions {
            onMapReady()
        }

        var dataList: List<BuildingItem> = emptyList()

        var loadingDataFromDataBaseIsDone = false

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.dataFlow.collect {
                    dataList = it
                    if (!loadingDataFromDataBaseIsDone) {
                        setMarkers(dataList)
                        loadingDataFromDataBaseIsDone = true
                    }
                }
            }
        }

        val timeLength = if (dataList.isEmpty()) // If not data in general (even in database)
            Snackbar.LENGTH_INDEFINITE
        else { // If no NEW data, but we have OLD data in database
            Snackbar.LENGTH_LONG
        }

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
                            setMarkers(dataList)
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

    private lateinit var annotationApi : AnnotationPlugin
    private lateinit var pointAnnotationManager : PointAnnotationManager
    private lateinit var viewAnnotationManager : ViewAnnotationManager

    private val asyncInflater by lazy { AsyncLayoutInflater(requireContext()) }

    private fun setMarkers(itemsList: List<BuildingItem>) {
        annotationApi = mapView.annotations
        pointAnnotationManager = annotationApi.createPointAnnotationManager()
        viewAnnotationManager = mapView.viewAnnotationManager
        for (item in itemsList) {
            if (item.address == null)
                continue
            // Здесь (в будущем) стоит добавить случаи для других типов зданий (дефолт - историческое)
            val buildingType = when (item.type) {
                "историческое" -> R.drawable.ic_marker_historical
                "учебное" -> R.drawable.ic_marker_uchebnoye
                "общежитие" -> R.drawable.ic_marker_obsezhitie
                "административное" -> R.drawable.ic_marker_admin
                "многофункциональное" -> R.drawable.ic_marker_mnogofunc
                else -> R.drawable.ic_marker_historical
            }
            val point = Point.fromLngLat(
                item.address?.longitude?.toDouble()!!,
                item.address?.latitude?.toDouble()!!)
            val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
                .withPoint(point)
                .withIconImage(ContextCompat.getDrawable(requireContext(), buildingType)?.toBitmap()!!)
            val pointAnnotation = pointAnnotationManager.create(pointAnnotationOptions)
            if (viewAnnotationManager.getViewAnnotationByFeatureId(pointAnnotation.featureIdentifier) == null) {
                if (item.type == "историческое") { // Лучше заменить на !isModern, но на сервере все isModern стоят false...
                    viewAnnotationManager.addViewAnnotation(
                        resId = R.layout.hist_icon_info,
                        options = viewAnnotationOptions {
                            geometry(point)
                            associatedFeatureId(pointAnnotation.featureIdentifier)
                            anchor(ViewAnnotationAnchor.BOTTOM)
                            selected(false)
                            allowOverlap(false)
                            color(requireContext().getColor(R.color.bsu))
                        },
                        asyncInflater = asyncInflater
                    ) { viewAnnotation ->
                        viewAnnotation.visibility = View.GONE
                        // calculate offsetY manually taking into account icon height only because of bottom anchoring
                        viewAnnotationManager.updateViewAnnotation(
                            viewAnnotation,
                            viewAnnotationOptions {
                                //offsetY(*(pointAnnotation.iconImageBitmap?.height!!).toInt())
                            }
                        )
                        HistIconInfoBinding.bind(viewAnnotation).apply {
                            histIconTitle.text = item.name
                            histIconBtnSeeHistoricalInformation.setOnClickListener {
                                // TODO вызвать onClick() из MainActivity
                            }
                            histIconBtnCreateRoute.setOnClickListener {
                                // TODO вызвать onClick() из MainActivity
                            }
                            histIconBtnSee3dModel.setOnClickListener {
                                // TODO вызвать onClick() из MainActivity
                            }
                        }
                    }
                } else {
                    viewAnnotationManager.addViewAnnotation(
                        resId = R.layout.modern_icon_info,
                        options = viewAnnotationOptions {
                            geometry(point)
                            associatedFeatureId(pointAnnotation.featureIdentifier)
                            anchor(ViewAnnotationAnchor.BOTTOM)
                            selected(false)
                            allowOverlap(false)
                            color(requireContext().getColor(R.color.bsu))
                        },
                        asyncInflater = asyncInflater
                    ) { viewAnnotation ->
                        viewAnnotation.visibility = View.GONE
                        // calculate offsetY manually taking into account icon height only because of bottom anchoring
                        viewAnnotationManager.updateViewAnnotation(
                            viewAnnotation,
                            viewAnnotationOptions {
                                //offsetY((pointAnnotation.iconImageBitmap?.height!!).toInt())
                            }
                        )
                        ModernIconInfoBinding.bind(viewAnnotation).apply {
                            modernIconTitle.text = item.name
                            if (item.structuralObjects.isNullOrEmpty()) {
                                iconEmptyDepartmentsLabel.isGone = false
                                iconRecyclerView.isGone = true
                            } else {
                                iconEmptyDepartmentsLabel.isGone = true
                                iconRecyclerView.isGone = false
                                val recyclerView = iconRecyclerView
                                recyclerView.layoutManager = GridLayoutManager(context, 1)
                                val adapter = DepartmentsListAdapter(requireContext())
                                recyclerView.adapter = adapter
                                adapter.submitList(item.structuralObjects)
                            }
                        }
                    }
                }
                pointAnnotationManager.addClickListener { clickedPoint ->
                    val iconView =
                        viewAnnotationManager.getViewAnnotationByFeatureId(clickedPoint.featureIdentifier)
                    iconView?.toggleViewVisibility()
                    val isSelected =
                        viewAnnotationManager.getViewAnnotationOptionsByFeatureId(clickedPoint.featureIdentifier)?.selected
                    if (iconView != null) {
                        viewAnnotationManager.updateViewAnnotation(
                            iconView,
                            viewAnnotationOptions {
                                if (isSelected == true)
                                    selected(false)
                                else
                                    selected(true)
                            }
                        )
                    }
                    true
                }
            }
        }
    }

    private fun View.toggleViewVisibility() {
        visibility = if (visibility == View.VISIBLE) View.GONE else View.VISIBLE
    }

    private fun onMapReady() {
        mapView.getMapboxMap().setCamera(
            CameraOptions.Builder()
                .zoom(14.0)
                .build()
        )
        mapView.getMapboxMap().loadStyleUri(
            Style.MAPBOX_STREETS
        ) {
            initLocationComponent()
            setupGesturesListener()
        }
    }

    private fun setupGesturesListener() {
        mapView.gestures.addOnMoveListener(onMoveListener)
    }

    private fun initLocationComponent() {
        val locationComponentPlugin = mapView.location
        locationComponentPlugin.updateSettings {
            this.enabled = true
            this.locationPuck = LocationPuck2D(
                bearingImage = AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.mapbox_user_puck_icon,
                ),
                shadowImage = AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.mapbox_user_icon_shadow,
                ),
                scaleExpression = interpolate {
                    linear()
                    zoom()
                    stop {
                        literal(0.0)
                        literal(0.6)
                    }
                    stop {
                        literal(20.0)
                        literal(1.0)
                    }
                }.toJson()
            )
        }
        locationComponentPlugin.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        locationComponentPlugin.addOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
    }

    private fun onCameraTrackingDismissed() {
        mapView.location
            .removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        mapView.location
            .removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        mapView.gestures.removeOnMoveListener(onMoveListener)
    }

    private fun createSnackbar(message: String, color: Int) {
        var timeLength = BaseTransientBottomBar.LENGTH_LONG
        if (message == getString(R.string.loading))
            timeLength = BaseTransientBottomBar.LENGTH_INDEFINITE
        val snackbar = Snackbar.make(
            binding.mapContainer,
            message,
            timeLength
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
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
        mapView.location
            .removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        mapView.location
            .removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        mapView.gestures.removeOnMoveListener(onMoveListener)
    }

    @Override
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _modernIconBinding = null
        _histIconBinding = null
    }

    @Override
    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

}