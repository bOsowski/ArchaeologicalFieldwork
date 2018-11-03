package org.wit.archaeologicalfieldwork.main

import android.app.Application
import org.jetbrains.anko.AnkoLogger
import org.wit.archaeologicalfieldwork.models.*
import org.wit.archaeologicalfieldwork.models.stores.*

class MainApp : Application(), AnkoLogger{

    lateinit var users: UserMemStore
    lateinit var currentUser: User
    lateinit var forts: HillfortMemStore
    lateinit var currentFort: Hillfort

    override fun onCreate() {
        super.onCreate()
        users = UserJSONStore(applicationContext)
        forts = HillfortJSONStore(applicationContext)
        var hillfort = Hillfort()
        hillfort.name = "Nice hillfort"
        hillfort.description = "it's very nice."
        forts.create(hillfort)
    }
}