package org.wit.archaeologicalfieldwork.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

import kotlinx.android.synthetic.main.activity_hillfort_maps.*
import kotlinx.android.synthetic.main.content_hillfort_maps.*
import org.wit.archaeologicalfieldwork.R
import org.wit.archaeologicalfieldwork.main.MainApp

class HillfortMapsActivity : AppCompatActivity() {

    lateinit var map: GoogleMap
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort_maps)
        setSupportActionBar(toolbar)
        mapView.onCreate(savedInstanceState)
        app = application as MainApp
        mapView.getMapAsync {
            map = it
            configureMap()
        }
    }

    fun configureMap(){
        map.uiSettings.isZoomControlsEnabled = true
        app.data.findAll().hillforts.forEach{
            val options = MarkerOptions().title(it.name).position(LatLng(it.location.lat, it.location.lng))
            map.addMarker(options).tag = it.id
        }
        val lastLocation = app.data.findAll().hillforts.last().location
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lastLocation.lat, lastLocation.lng), lastLocation.zoom))
    }

    override fun onDestroy(){
        super.onDestroy()
        mapView.onDestroy()
    }


    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onPause(){
        super.onPause()
        mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle?){
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}
