package org.wit.archaeologicalfieldwork.views.hillfort

import androidx.recyclerview.widget.LinearLayoutManager
import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.*
import org.wit.archaeologicalfieldwork.R
import org.wit.archaeologicalfieldwork.activities.NotesActivity
import org.wit.archaeologicalfieldwork.adapters.ImageAdapter
import org.wit.archaeologicalfieldwork.helpers.simplifyDate
import org.wit.archaeologicalfieldwork.main.MainApp
import org.wit.archaeologicalfieldwork.models.Hillfort
import org.wit.archaeologicalfieldwork.views.BaseView

class HillfortView : BaseView(), AnkoLogger {

    private lateinit var presenter: HillfortPresenter
    lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort)
        mapView2.onCreate(savedInstanceState)
        mapView2.getMapAsync {
            map = it
            map.setOnMapClickListener { _ ->
                presenter.doSetLocation()
            }
            presenter.doConfigureMap(map)
            //mapView2.onResume() //this line fixes a bug where the map would not properly load.
        }
//        The title would obstruct buttons for this view.
//        toolbarAdd.title = title
//        setSupportActionBar(toolbarAdd)

        presenter = initPresenter(HillfortPresenter(this)) as HillfortPresenter

        cancel.setOnClickListener{
            presenter.doCancel()
        }

        visitedCheckBox.setOnClickListener {
            presenter.doSetVisited()
        }

        btnSave.setOnClickListener {
            presenter.doAddOrSave()
        }

        chooseImage.setOnClickListener {
            presenter.doSelectImage()
        }

        delete.setOnClickListener {
            presenter.doDelete()
        }

        viewNotes.setOnClickListener {
            info("View notes clicked.")
            startActivity(intentFor<NotesActivity>().putExtra("hillfort_data", presenter.hillfort))
            finish()
        }
    }

    override fun showHillfort(hillfort: Hillfort){
        val layoutManager = LinearLayoutManager(this)
        recyclerViewImages.layoutManager = layoutManager

        hillfortName.setText(hillfort.name)
        hillfortDescription.setText(hillfort.description)
        val visit = hillfort.visits.find { it.userId == (application as MainApp).currentUser.id }
        if(visit != null){
            visitedCheckBox.text = resources.getString(R.string.visited_time, simplifyDate(visit.date))
        }
        visitedCheckBox.isChecked = visit != null
        showImages(hillfort.images)
    }

    fun showImages(images: List<String>){
        recyclerViewImages.adapter = ImageAdapter(images, this)
        recyclerViewImages.adapter?.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        mapView2.onResume()
        presenter.doResartLocationUpdates()
    }
}
