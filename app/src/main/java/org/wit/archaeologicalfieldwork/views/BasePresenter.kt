package org.wit.archaeologicalfieldwork.views

import android.content.Intent
import org.wit.archaeologicalfieldwork.main.MainApp

open class BasePresenter(var view: BaseView?) {

    var app: MainApp = view?.application as MainApp

    open fun doActivityResult(requestCode: Int, data: Intent?){

    }

    open fun doRequestPermissionsResult(requestCode: Int, permission: Array<String>, grantResults: IntArray){

    }

    open fun onDestroy(){
        view = null
    }

    open fun doEditImage(image: String) {

    }
}