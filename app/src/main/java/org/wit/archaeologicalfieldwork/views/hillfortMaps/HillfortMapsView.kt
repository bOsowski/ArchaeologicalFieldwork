package org.wit.archaeologicalfieldwork.views.hillfortMaps

import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.activity_hillfort_maps.*
import kotlinx.android.synthetic.main.content_hillfort_maps.*
import org.wit.archaeologicalfieldwork.R
import org.wit.archaeologicalfieldwork.views.BaseView

class HillfortMapsView : BaseView(), GoogleMap.OnMarkerClickListener {

    lateinit var map: GoogleMap
    private lateinit var presenter: HillfortMapsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort_maps)
        setSupportActionBar(toolbar)
        mapView.onCreate(savedInstanceState)
        presenter = initPresenter(HillfortMapsPresenter(this)) as HillfortMapsPresenter
        mapView.getMapAsync {
            map = it
            presenter.initMap(map)
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        presenter.doUpdateContent(marker)
        return false
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

    override fun onSaveInstanceState(outState: Bundle){
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}
