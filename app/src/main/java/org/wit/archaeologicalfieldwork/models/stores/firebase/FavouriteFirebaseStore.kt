package org.wit.archaeologicalfieldwork.models.stores.firebase

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.jetbrains.anko.AnkoLogger
import org.wit.archaeologicalfieldwork.models.Favourite
import org.wit.archaeologicalfieldwork.models.stores.Store

class FavouriteFirebaseStore(val context: Context) : Store<Favourite>, AnkoLogger {

    val favourites = ArrayList<Favourite>()
    lateinit var db: DatabaseReference

    override suspend fun findAll(): List<Favourite> {
        return favourites
    }

    override suspend fun find(id: Long): Favourite? {
        return favourites.find { it.id == id }
    }

    override suspend fun create(item: Favourite): Long {
        val key = db.child("favourites").push().key
        item.fbId = key!!
        item.id = key.hashCode().toLong()
        favourites.add(item)
        db.child("favourites").child(key).setValue(item)
        return item.id
    }

    override suspend fun update(item: Favourite) {
        var foundFavourite: Favourite? = favourites.find { it.fbId == item.fbId }
        if(foundFavourite != null){
            foundFavourite.date = item.date
        }
        db.child("favourites").child(item.fbId).setValue(foundFavourite)
    }

    override suspend fun delete(item: Favourite) {
        db.child("favourites").child(item.fbId).removeValue()
        favourites.remove(item)
    }

    fun fetchFavourites(favouritesReady: () -> Unit){
        val valueEvenListener = object : ValueEventListener{
            override fun onCancelled(error: DatabaseError){
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.mapNotNullTo(favourites) {it.getValue<Favourite>(Favourite::class.java)}
                favouritesReady()
            }
        }
        db = FirebaseDatabase.getInstance().reference
        db.child("favourites").addListenerForSingleValueEvent(valueEvenListener)
    }
}