package org.wit.archaeologicalfieldwork.views.hillfortMaps

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.content_hillfort_maps.*
import org.wit.archaeologicalfieldwork.helpers.readImageFromPath
import org.wit.archaeologicalfieldwork.views.BasePresenter

class HillfortMapsPresenter(view: HillfortMapsView) : BasePresenter(view){

    fun initMap(map: GoogleMap){
        map.uiSettings.isZoomControlsEnabled = true
        app.data.findAll().hillforts.forEach{
            val options = MarkerOptions().title(it.name).position(LatLng(it.location.lat, it.location.lng))
            map.addMarker(options).tag = it.id
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.location.lat, it.location.lng), it.location.zoom))
            map.setOnMarkerClickListener(view as HillfortMapsView)
        }
    }

    fun doUpdateContent(marker: Marker) {
        view?.currentTitle!!.text = marker.title
        val foundHillfort = app.data.findAll().hillforts.find{ it.id == marker.tag }
        if(foundHillfort != null && !foundHillfort.images.isEmpty()){
            view?.hillfortMapImage!!.setImageBitmap(readImageFromPath(view!!, foundHillfort.images.first()))
        }
        view?.hillfortMapDescription!!.text = foundHillfort?.description
    }
}