package org.wit.archaeologicalfieldwork.main

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.jetbrains.anko.AnkoLogger
import org.wit.archaeologicalfieldwork.models.*
import org.wit.archaeologicalfieldwork.models.stores.Store
import org.wit.archaeologicalfieldwork.models.stores.firebase.*
import org.wit.archaeologicalfieldwork.models.stores.room.*

class MainApp : Application(), AnkoLogger{

    lateinit var hillforts: Store<Hillfort>
    lateinit var images: Store<Image>
    lateinit var notes: Store<Note>
    lateinit var visits: Store<Visit>
    lateinit var ratings: Store<Rating>
    lateinit var favourites: Store<Favourite>
    lateinit var user: FirebaseUser



    override fun onCreate() {
        super.onCreate()
        hillforts = HillfortFirebaseStore(applicationContext)
        images = ImageFirebaseStore(applicationContext)
        notes = NoteFirebaseStore(applicationContext)
        visits = VisitFirebaseStore(applicationContext)
        ratings = RatingFirebaseStore(applicationContext)
        favourites = FavouriteFirebaseStore(applicationContext)

        user = FirebaseAuth.getInstance().currentUser!!

        //hillforts = HillfortStoreRoom(applicationContext)
//        images = ImageStoreRoom(applicationContext)
//        notes = NoteStoreRoom(applicationContext)
//        visits = VisitStoreRoom(applicationContext)



    }
}