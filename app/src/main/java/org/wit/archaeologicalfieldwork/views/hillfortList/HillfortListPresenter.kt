package org.wit.archaeologicalfieldwork.views.hillfortList

import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.wit.archaeologicalfieldwork.activities.SettingsActivity
import org.wit.archaeologicalfieldwork.helpers.hillfortRefreshTime
import org.wit.archaeologicalfieldwork.views.hillfortMaps.HillfortMapsView
import org.wit.archaeologicalfieldwork.models.Hillfort
import org.wit.archaeologicalfieldwork.models.stores.firebase.HillfortFirebaseStore
import org.wit.archaeologicalfieldwork.models.stores.firebase.ImageFirebaseStore
import org.wit.archaeologicalfieldwork.views.BasePresenter
import org.wit.archaeologicalfieldwork.views.BaseView
import org.wit.archaeologicalfieldwork.views.VIEW
import org.wit.archaeologicalfieldwork.views.hillfort.HillfortView
import java.util.*
import kotlin.concurrent.timerTask

class HillfortListPresenter(view: BaseView) : BasePresenter(view), AnkoLogger{

    fun loadHillforts(){
     async(UI){
        view?.showHillforts(app.hillforts.findAll())
     }
    }

    init{
        //show data changes if using firebase
        if(app.hillforts is HillfortFirebaseStore) {
            Timer().schedule(timerTask {
                (app.hillforts as HillfortFirebaseStore).fetchHillforts {
                    loadHillforts()
                }
                //if images fetch before hillforts do, they will be simply loaded in and ready for when hillforts do, but this is unlikely.
                //if images fetch after hillforts, the view will simply reload with loaded images.
                (app.images as ImageFirebaseStore).fetchImages {
                    loadHillforts()
                }
            }, 0, hillfortRefreshTime)
        }
    }

    fun doAddHillfort(){
        view?.startActivityForResult<HillfortView>(0)
    }

    fun doEditHillfort(hillfort: Hillfort){
        view?.navigateTo(VIEW.HILLFORT, 0, "hillfort_edit", hillfort)
    }

    fun doShowHillfortsMap(){
        view?.startActivity<HillfortMapsView>()
    }

    fun doViewSettings(){
        view?.startActivity<SettingsActivity>()
    }
}