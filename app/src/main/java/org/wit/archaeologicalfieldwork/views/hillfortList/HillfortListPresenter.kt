package org.wit.archaeologicalfieldwork.views.hillfortList

import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.wit.archaeologicalfieldwork.views.hillfortMaps.HillfortMapsView
import org.wit.archaeologicalfieldwork.main.MainApp
import org.wit.archaeologicalfieldwork.models.Hillfort
import org.wit.archaeologicalfieldwork.views.hillfort.HillfortView

class HillfortListPresenter(private val view: HillfortListView){

    var app: MainApp = view.application as MainApp

    fun getHillforts() = app.data.findAll().hillforts

    fun doAddHillfort(){
        app.currentFort = Hillfort()
        view.startActivityForResult<HillfortView>(0)
    }

    fun doEditHillfort(hillfort: Hillfort){
        app.currentFort = hillfort
        view.startActivityForResult(view.intentFor<HillfortView>().putExtra("hillfort_edit", hillfort), 0)
    }

    fun doShowHillfortsMap(){
        view.startActivity<HillfortMapsView>()
    }
}