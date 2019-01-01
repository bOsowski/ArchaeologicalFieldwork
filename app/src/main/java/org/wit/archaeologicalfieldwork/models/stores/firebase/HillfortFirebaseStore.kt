package org.wit.archaeologicalfieldwork.models.stores.firebase

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.archaeologicalfieldwork.models.Hillfort
import org.wit.archaeologicalfieldwork.models.stores.Store

class HillfortFirebaseStore(val context: Context) : Store<Hillfort>, AnkoLogger {

    val hillforts = ArrayList<Hillfort>()
    lateinit var userId: String
    lateinit var db: DatabaseReference

    override suspend fun findAll(): List<Hillfort> {
        return hillforts
    }

    override suspend fun find(id: Long): Hillfort? {
        return hillforts.find { it.id == id }
    }

    override suspend fun create(item: Hillfort): Long {
        val key = db.child("hillforts").push().key
        item.fbId = key!!
        item.id = key.hashCode().toLong()
        hillforts.add(item)
        db.child("hillforts").child(key).setValue(item)
        return item.id
    }

    override suspend fun update(item: Hillfort) {
        var foundHillfort: Hillfort? = hillforts.find { it.fbId == item.fbId }
        if(foundHillfort != null){
            foundHillfort.name = item.name
            foundHillfort.description = item.description
            foundHillfort.location = item.location
        }
        db.child("hillforts").child(item.fbId).setValue(foundHillfort)
    }

    override suspend fun delete(item: Hillfort) {
        db.child("hillforts").child(item.fbId).removeValue()
        hillforts.remove(item)
    }

    fun fetchHillforts(hillfortsReady: () -> Unit){
        val valueEvenListener = object : ValueEventListener{
            override fun onCancelled(error: DatabaseError){
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                hillforts.clear()
                dataSnapshot.children.mapNotNullTo(hillforts) {it.getValue<Hillfort>(Hillfort::class.java)}
                hillfortsReady()
                info("Hillforts ready. fetched ${hillforts.size} hillforts.")
            }
        }
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance().reference
        db.child("hillforts").addListenerForSingleValueEvent(valueEvenListener)
    }
}