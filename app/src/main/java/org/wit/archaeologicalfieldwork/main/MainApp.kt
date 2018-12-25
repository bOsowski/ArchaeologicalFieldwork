package org.wit.archaeologicalfieldwork.main

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.jetbrains.anko.AnkoLogger
import org.wit.archaeologicalfieldwork.models.*
import org.wit.archaeologicalfieldwork.models.stores.room.*

class MainApp : Application(), AnkoLogger{

    lateinit var hillforts: HillfortStoreRoom
    lateinit var images: ImageStoreRoom
    lateinit var notes: NoteStoreRoom
    lateinit var visits: VisitStoreRoom
    lateinit var user: FirebaseUser
//    lateinit var currentFort: Hillfort

    override fun onCreate() {
        super.onCreate()
        hillforts = HillfortStoreRoom(applicationContext)
        images = ImageStoreRoom(applicationContext)
        notes = NoteStoreRoom(applicationContext)
        visits = VisitStoreRoom(applicationContext)
        user = FirebaseAuth.getInstance().currentUser!!
    }
}