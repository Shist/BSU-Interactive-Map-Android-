package com.example.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

class MainActivity : AppCompatActivity() {

    private lateinit var map: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val ctx = applicationContext;
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))

        setContentView(R.layout.activity_main)

        map = findViewById(R.id.map)
        map.setTileSource(TileSourceFactory.MAPNIK)

        map.setMultiTouchControls(true)

        val pointBelarus = GeoPoint(53.894,27.547)

        map.controller.setCenter(pointBelarus)
        map.controller.setZoom(12.0)

        val pointInfo = map.mapCenter.toString()
        val zoomInfo = map.zoomLevelDouble.toString()
        Log.d("point_on_map", pointInfo)
        Log.d("zoom_on_map", zoomInfo)
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

//базовый путь:
//map.bsu.by:51107
//
///api/buildings/all
//
//return all buildings info:
//- address with coordinates
//- type of buildings
//- all related structural objects with icons and categories
//
//
///api/buildings/modern
//
//return only modern buildings (with the same info as above) which are listed here:
//https://docs.google.com/spreadsheets/d/1eORmT9kGK5tZ6WwoS9SYc5YPshkdYLIe/edit#gid=1698269244
//
//
///api/buildings/historical
//
//
//return only historical buildings info:
//- address with coordinates
//
//
///api/building-photos?buildingId={id}
//
//return all building photos (work for both modern and historical buildings) for specified building id
//
///api/structural-objects/all
//
//return all structural objects, which are listed here:
//https://docs.google.com/spreadsheets/d/1eORmT9kGK5tZ6WwoS9SYc5YPshkdYLIe/edit#gid=2004203366