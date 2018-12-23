package org.wit.archaeologicalfieldwork.views.editLocation

import android.app.Activity
import android.content.Intent
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.wit.archaeologicalfieldwork.models.Location
import org.wit.archaeologicalfieldwork.views.BasePresenter

class EditLocationPresenter(view: EditLocationView) : BasePresenter(view){

    var location = Location()

    init{
        location = view.intent.extras.getParcelable("location")
    }

    fun initMap(map: GoogleMap){
        val loc = LatLng(location.lat, location.lng)
        val options = MarkerOptions()
            .title("Hillfort")
            .snippet("GPS : " + loc.toString())
            .draggable(true)
            .position(loc)
        map.addMarker(options)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, location.zoom))
    }

    fun doUpdateLocation(lat: Double, lng: Double, zoom: Float){
        location.lat = lat
        location.lng = lng
        location.zoom = zoom
    }

    fun doOnBackPressed(){
        val resultIntent = Intent()
        resultIntent.putExtra("location", location)
        view?.setResult(Activity.RESULT_OK, resultIntent)
        view?.finish()
    }

    fun doUpdateMarker(marker: Marker){
        marker.snippet = ("GPS : " + LatLng(location.lat, location.lng).toString())
        marker.showInfoWindow()
    }
}