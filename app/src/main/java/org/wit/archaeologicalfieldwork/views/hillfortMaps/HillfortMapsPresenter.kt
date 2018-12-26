package org.wit.archaeologicalfieldwork.views.hillfortMaps

import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.content_hillfort_maps.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.wit.archaeologicalfieldwork.views.BasePresenter

class HillfortMapsPresenter(view: HillfortMapsView) : BasePresenter(view){

    fun initMap(map: GoogleMap){
        map.uiSettings.isZoomControlsEnabled = true
        async(UI){
            app.hillforts.findAll().forEach{
                val options = MarkerOptions().title(it.name).position(LatLng(it.location.lat, it.location.lng))
                map.addMarker(options).tag = it
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.location.lat, it.location.lng), 0f))
            }
            map.setOnMarkerClickListener(view as HillfortMapsView)
        }
    }

    fun doUpdateContent(marker: Marker) {
        view?.currentTitle!!.text = marker.title
        async(UI) {
            val foundHillfort = app.hillforts.find(marker.tag as Long)
            val images = app.images.findAll().filter { it.hillfortId == foundHillfort.id }
            if(!images.isEmpty()){
                Glide.with(view!!).load(images.first().data).into(view?.hillfortMapImage!!)
            }
            view?.hillfortMapDescription!!.text = foundHillfort.description
        }
    }
}