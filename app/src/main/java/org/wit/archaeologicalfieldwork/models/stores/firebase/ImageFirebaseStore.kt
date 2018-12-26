package org.wit.archaeologicalfieldwork.models.stores.firebase

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.jetbrains.anko.AnkoLogger
import org.wit.archaeologicalfieldwork.models.Image
import org.wit.archaeologicalfieldwork.models.stores.Store

class ImageFirebaseStore(val context: Context) : Store<Image>, AnkoLogger {

    val images = ArrayList<Image>()
    lateinit var userId: String
    lateinit var db: DatabaseReference

    override suspend fun findAll(): List<Image> {
        return images
    }

    override suspend fun find(id: Long): Image? {
        return images.find { it.id == id }
    }

    override suspend fun create(item: Image): Long {
        val key = db.child("images").push().key
        item.fbId = key!!
        item.id = key.hashCode().toLong()
        images.add(item)
        db.child("images").child(key).setValue(item)
        return item.id
    }

    override suspend fun update(item: Image) {
        var foundImage: Image? = images.find { it.fbId == item.fbId }
        if(foundImage != null){
            foundImage.data = item.data
        }
        db.child("images").child(item.fbId).setValue(foundImage)
    }

    override suspend fun delete(item: Image) {
        db.child("images").child(item.fbId).removeValue()
        images.remove(item)
    }

    fun fetchImages(imagesReady: () -> Unit){
        val valueEvenListener = object : ValueEventListener{
            override fun onCancelled(error: DatabaseError){
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.mapNotNullTo(images) {it.getValue<Image>(Image::class.java)}
                imagesReady()
            }
        }
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance().reference
        db.child("images").addListenerForSingleValueEvent(valueEvenListener)
    }
}