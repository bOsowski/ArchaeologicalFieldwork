package org.wit.archaeologicalfieldwork.main

import android.app.Application
import org.jetbrains.anko.AnkoLogger
import org.wit.archaeologicalfieldwork.models.*
import org.wit.archaeologicalfieldwork.models.stores.Store

class MainApp : Application(), AnkoLogger{

    lateinit var users: Store<User>
    lateinit var forts: Store<Hillfort>
    lateinit var visits: Store<Visit>

    override fun onCreate() {
        super.onCreate()
    }
}