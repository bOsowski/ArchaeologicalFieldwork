package org.wit.archaeologicalfieldwork.main

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.jetbrains.anko.AnkoLogger
import org.wit.archaeologicalfieldwork.models.*
import org.wit.archaeologicalfieldwork.models.stores.Store
import org.wit.archaeologicalfieldwork.models.stores.firebase.HillfortFirebaseStore
import org.wit.archaeologicalfieldwork.models.stores.firebase.ImageFirebaseStore
import org.wit.archaeologicalfieldwork.models.stores.firebase.NoteFirebaseStore
import org.wit.archaeologicalfieldwork.models.stores.firebase.VisitFirebaseStore
import org.wit.archaeologicalfieldwork.models.stores.room.*

class MainApp : Application(), AnkoLogger{

    lateinit var hillforts: Store<Hillfort>
    lateinit var images: Store<Image>
    lateinit var notes: Store<Note>
    lateinit var visits: Store<Visit>
    lateinit var user: FirebaseUser

    override fun onCreate() {
        super.onCreate()
        hillforts = HillfortFirebaseStore(applicationContext)
        images = ImageFirebaseStore(applicationContext)
        notes = NoteFirebaseStore(applicationContext)
        visits = VisitFirebaseStore(applicationContext)

        //hillforts = HillfortStoreRoom(applicationContext)
//        images = ImageStoreRoom(applicationContext)
//        notes = NoteStoreRoom(applicationContext)
//        visits = VisitStoreRoom(applicationContext)

        user = FirebaseAuth.getInstance().currentUser!!

        if(hillforts is HillfortFirebaseStore) {
            (hillforts as HillfortFirebaseStore).fetchHillforts {

            }
        }
        if(images is ImageFirebaseStore) {
            (images as ImageFirebaseStore).fetchImages {

            }
        }
        if(notes is NoteFirebaseStore) {
            (notes as NoteFirebaseStore).fetchNotes {

            }
        }
        if(visits is VisitFirebaseStore) {
            (visits as VisitFirebaseStore).fetchVisits {

            }
        }
    }
}