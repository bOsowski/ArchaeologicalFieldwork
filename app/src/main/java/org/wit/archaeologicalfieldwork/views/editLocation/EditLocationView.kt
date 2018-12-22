package org.wit.archaeologicalfieldwork.views.editLocation

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import org.jetbrains.anko.AnkoLogger
import org.wit.archaeologicalfieldwork.R
import org.wit.archaeologicalfieldwork.models.Location

class EditLocationView : AppCompatActivity(), GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener, AnkoLogger {

    private lateinit var presenter: EditLocationPresenter
    private lateinit var map: GoogleMap

    override fun onMarkerClick(marker: Marker): Boolean {
        presenter.doUpdateMarker(marker)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.lat, location.lng), location.zoom))
        return false
    }

    override fun onMarkerDragStart(marker: Marker) {
    }

    override fun onMarkerDrag(marker: Marker) {
    }

    override fun onMarkerDragEnd(marker: Marker) {
        presenter.doUpdateLocation(marker.position.latitude, marker.position.longitude, map.cameraPosition.zoom)
    }

    var location = Location()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_location)
        location = intent.extras.getParcelable("location")
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        presenter = EditLocationPresenter(this)
        mapFragment.getMapAsync{
            map = it
            map.setOnMarkerClickListener(this)
            map.setOnMarkerDragListener(this)
            presenter.initMap(map)
        }
    }

    override fun onBackPressed() {
        presenter.doOnBackPressed()
    }
}