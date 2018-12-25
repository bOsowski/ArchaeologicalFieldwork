package org.wit.archaeologicalfieldwork.main

import android.app.Application
import org.jetbrains.anko.AnkoLogger
import org.wit.archaeologicalfieldwork.models.*
import org.wit.archaeologicalfieldwork.models.stores.room.HillfortStoreRoom

class MainApp : Application(), AnkoLogger{

    lateinit var data: HillfortStoreRoom
    lateinit var currentUser: User
//    lateinit var currentFort: Hillfort

    override fun onCreate() {
        super.onCreate()
        data = HillfortStoreRoom(applicationContext)
    }
}