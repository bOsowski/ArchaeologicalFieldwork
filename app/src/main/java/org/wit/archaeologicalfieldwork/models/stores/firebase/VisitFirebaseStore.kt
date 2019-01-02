package org.wit.archaeologicalfieldwork.models.stores.firebase

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.jetbrains.anko.AnkoLogger
import org.wit.archaeologicalfieldwork.models.Visit
import org.wit.archaeologicalfieldwork.models.stores.Store

class VisitFirebaseStore(val context: Context) : Store<Visit>, AnkoLogger {

    val visits = ArrayList<Visit>()
    lateinit var db: DatabaseReference

    override suspend fun findAll(): List<Visit> {
        return visits
    }

    override suspend fun find(id: Long): Visit? {
        return visits.find { it.id == id }
    }

    override suspend fun create(item: Visit): Long {
        val key = db.child("visits").push().key
        item.fbId = key!!
        item.id = key.hashCode().toLong()
        visits.add(item)
        db.child("visits").child(key).setValue(item)
        return item.id
    }

    override suspend fun update(item: Visit) {
        var foundVisit: Visit? = visits.find { it.fbId == item.fbId }
        if(foundVisit != null){
            foundVisit.date = item.date
        }
        db.child("visits").child(item.fbId).setValue(foundVisit)
    }

    override suspend fun delete(item: Visit) {
        db.child("visits").child(item.fbId).removeValue()
        visits.remove(item)
    }

    fun fetchVisits(visitsReady: () -> Unit){
        val valueEvenListener = object : ValueEventListener{
            override fun onCancelled(error: DatabaseError){
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                visits.clear()
                dataSnapshot.children.mapNotNullTo(visits) {it.getValue<Visit>(Visit::class.java)}
                visitsReady()
            }
        }
        db = FirebaseDatabase.getInstance().reference
        db.child("visits").addListenerForSingleValueEvent(valueEvenListener)
    }
}