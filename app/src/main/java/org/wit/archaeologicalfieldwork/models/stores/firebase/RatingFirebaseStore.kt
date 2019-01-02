package org.wit.archaeologicalfieldwork.models.stores.firebase

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.jetbrains.anko.AnkoLogger
import org.wit.archaeologicalfieldwork.models.Rating
import org.wit.archaeologicalfieldwork.models.stores.Store

class RatingFirebaseStore(val context: Context) : Store<Rating>, AnkoLogger {

    val ratings = ArrayList<Rating>()
    lateinit var db: DatabaseReference

    override suspend fun findAll(): List<Rating> {
        return ratings
    }

    override suspend fun find(id: Long): Rating? {
        return ratings.find { it.id == id }
    }

    override suspend fun create(item: Rating): Long {
        val key = db.child("ratings").push().key
        item.fbId = key!!
        item.id = key.hashCode().toLong()
        ratings.add(item)
        db.child("ratings").child(key).setValue(item)
        return item.id
    }

    override suspend fun update(item: Rating) {
        var foundRating: Rating? = ratings.find { it.fbId == item.fbId }
        if(foundRating != null){
            foundRating.date = item.date
        }
        db.child("ratings").child(item.fbId).setValue(foundRating)
    }

    override suspend fun delete(item: Rating) {
        db.child("ratings").child(item.fbId).removeValue()
        ratings.remove(item)
    }

    fun fetchRatings(ratingsReady: () -> Unit){
        val valueEvenListener = object : ValueEventListener{
            override fun onCancelled(error: DatabaseError){
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.mapNotNullTo(ratings) {it.getValue<Rating>(Rating::class.java)}
                ratingsReady()
            }
        }
        db = FirebaseDatabase.getInstance().reference
        db.child("ratings").addListenerForSingleValueEvent(valueEvenListener)
    }
}