package org.wit.archaeologicalfieldwork.views.hillfortList

import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.wit.archaeologicalfieldwork.views.hillfortMaps.HillfortMapsView
import org.wit.archaeologicalfieldwork.models.Hillfort
import org.wit.archaeologicalfieldwork.views.BasePresenter
import org.wit.archaeologicalfieldwork.views.BaseView
import org.wit.archaeologicalfieldwork.views.VIEW
import org.wit.archaeologicalfieldwork.views.hillfort.HillfortView

class HillfortListPresenter(view: BaseView) : BasePresenter(view){

    fun getHillforts() = app.data.findAll().hillforts

    fun doAddHillfort(){
        view?.startActivityForResult<HillfortView>(0)
    }

    fun doEditHillfort(hillfort: Hillfort){
        view?.navigateTo(VIEW.HILLFORT, 0, "hillfort_edit", hillfort)
    }

    fun doShowHillfortsMap(){
        view?.startActivity<HillfortMapsView>()
    }
}