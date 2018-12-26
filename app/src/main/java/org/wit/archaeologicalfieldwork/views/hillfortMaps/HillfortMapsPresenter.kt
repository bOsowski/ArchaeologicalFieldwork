package org.wit.archaeologicalfieldwork.views.hillfortMaps

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.content_hillfort_maps.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.wit.archaeologicalfieldwork.helpers.readImageFromPath
import org.wit.archaeologicalfieldwork.views.BasePresenter

class HillfortMapsPresenter(view: HillfortMapsView) : BasePresenter(view){

    fun initMap(map: GoogleMap){
        map.uiSettings.isZoomControlsEnabled = true
        async(UI){
            app.hillforts.findAll().forEach{
                val options = MarkerOptions().title(it.name).position(LatLng(it.location.lat, it.location.lng))
                map.addMarker(options).tag = it.id
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.location.lat, it.location.lng), 0f))
            }
            map.setOnMarkerClickListener(view as HillfortMapsView)
        }
    }

    fun doUpdateContent(marker: Marker) {
        view?.currentTitle!!.text = marker.title
        async(UI) {
            val foundHillfort = app.hillforts.findAll().find { it.id == marker.tag }
            if(foundHillfort != null){
                val images = app.images.findAll().filter { it.hillfortId == foundHillfort.id }
                if(!images.isEmpty()){
                    view?.hillfortMapImage!!.setImageBitmap(readImageFromPath(view!!, images.first().data))
                }
            }
            view?.hillfortMapDescription!!.text = foundHillfort?.description
        }
    }
}