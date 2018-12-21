package org.wit.archaeologicalfieldwork.main

import android.app.Application
import org.jetbrains.anko.AnkoLogger
import org.wit.archaeologicalfieldwork.models.*
import org.wit.archaeologicalfieldwork.models.stores.*

class MainApp : Application(), AnkoLogger{

    lateinit var data: UnifiedMemStore
    lateinit var currentUser: User
    lateinit var currentFort: Hillfort

    override fun onCreate() {
        super.onCreate()
        data = /*UnifiedMemStore()*/UnifiedJSONStore(applicationContext)
    }
}