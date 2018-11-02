package org.wit.archaeologicalfieldwork.main

import android.app.Application
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.intentFor
import org.wit.archaeologicalfieldwork.models.Hillfort
import org.wit.archaeologicalfieldwork.models.User
import org.wit.archaeologicalfieldwork.models.stores.Store
import java.lang.Thread.sleep

class MainApp : Application(), AnkoLogger{

    lateinit var users: Store<User>
    lateinit var forts: Store<Hillfort>

    override fun onCreate() {
        super.onCreate()
    }
}