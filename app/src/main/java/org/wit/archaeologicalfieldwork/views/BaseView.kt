package org.wit.archaeologicalfieldwork.views

import android.content.Intent
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import org.jetbrains.anko.AnkoLogger
import org.wit.archaeologicalfieldwork.adapters.ImageListener
import org.wit.archaeologicalfieldwork.models.Hillfort
import org.wit.archaeologicalfieldwork.views.editLocation.EditLocationView
import org.wit.archaeologicalfieldwork.views.hillfort.HillfortView
import org.wit.archaeologicalfieldwork.views.hillfortList.HillfortListView
import org.wit.archaeologicalfieldwork.views.hillfortMaps.HillfortMapsView

val ADD_IMAGE_REQUEST = 1
val IMAGE_EDIT_REQUEST = 2
val LOCATION_REQUEST = 3

enum class VIEW {
    LOCATION, HILLFORT, MAPS, LIST
}

open abstract class BaseView : AppCompatActivity(), AnkoLogger, ImageListener {

    private var basePresenter: BasePresenter? = null

    fun navigateTo(view: VIEW, code: Int = 0, key: String = "", value: Parcelable? = null){
        var intent: Intent = when(view){
            VIEW.LOCATION -> Intent(this, EditLocationView::class.java)
            VIEW.HILLFORT -> Intent(this, HillfortView::class.java)
            VIEW.MAPS -> Intent(this, HillfortMapsView::class.java)
            VIEW.LIST -> Intent(this, HillfortListView::class.java)
        }
        if(key != ""){
            intent.putExtra(key, value)
        }
        startActivityForResult(intent, code)
    }

    fun initPresenter(presenter: BasePresenter): BasePresenter{
        basePresenter = presenter
        return presenter
    }

    fun init(toolbar: Toolbar){
        toolbar.title = title
        setSupportActionBar(toolbar)
    }

    override fun onDestroy(){
        basePresenter?.onDestroy()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        basePresenter?.doActivityResult(requestCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        basePresenter?.doRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onImageClick(image: String) {
        basePresenter?.doEditImage(image)
    }

    open fun showHillfort(hillfort: Hillfort){}
    open fun showHillforts(hillforts: List<Hillfort>){}
    open fun showProgress(){}
    open fun hideProgress(){}
}
