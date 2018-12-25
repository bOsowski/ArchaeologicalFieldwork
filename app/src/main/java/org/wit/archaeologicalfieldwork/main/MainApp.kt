package org.wit.archaeologicalfieldwork.main

import android.app.Application
import org.jetbrains.anko.AnkoLogger
import org.wit.archaeologicalfieldwork.models.*
import org.wit.archaeologicalfieldwork.models.stores.room.*

class MainApp : Application(), AnkoLogger{

    lateinit var hillforts: HillfortStoreRoom
    lateinit var images: ImageStoreRoom
    lateinit var notes: NoteStoreRoom
    lateinit var users: UserStoreRoom
    lateinit var visits: VisitStoreRoom

    lateinit var currentUser: User
//    lateinit var currentFort: Hillfort

    override fun onCreate() {
        super.onCreate()
        hillforts = HillfortStoreRoom(applicationContext)
        images = ImageStoreRoom(applicationContext)
        notes = NoteStoreRoom(applicationContext)
        users = UserStoreRoom(applicationContext)
        visits = VisitStoreRoom(applicationContext)
    }
}