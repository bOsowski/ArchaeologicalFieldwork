package org.wit.archaeologicalfieldwork.models.stores.firebase

import android.content.Context
import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.jetbrains.anko.AnkoLogger
import org.wit.archaeologicalfieldwork.helpers.readImageFromPath
import org.wit.archaeologicalfieldwork.models.Image
import org.wit.archaeologicalfieldwork.models.stores.Store
import java.io.ByteArrayOutputStream
import java.io.File


class ImageFirebaseStore(val context: Context) : Store<Image>, AnkoLogger {

    val images = ArrayList<Image>()
    lateinit var userId: String
    lateinit var db: DatabaseReference
    lateinit var st: StorageReference

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
        updateImage(item)
        return item.id
    }

    suspend fun create(item: Image, bitmap: Bitmap): Long {
        val key = db.child("images").push().key
        item.fbId = key!!
        item.id = key.hashCode().toLong()
        images.add(item)
        db.child("images").child(key).setValue(item)
        updateImage(item, bitmap)
        return item.id
    }

    override suspend fun update(item: Image) {
        var foundImage: Image? = images.find { it.fbId == item.fbId }
        if(foundImage != null){
            foundImage.data = item.data
        }
        db.child("images").child(item.fbId).setValue(foundImage)
        updateImage(item)
    }

    override suspend fun delete(item: Image) {
        db.child("images").child(item.fbId).removeValue()
        images.remove(item)
    }

    fun fetchImages(imagesReady: () -> Unit){
        val valueEvenListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError){
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                images.clear()
                dataSnapshot.children.mapNotNullTo(images) {it.getValue<Image>(Image::class.java)}
                imagesReady()
            }
        }
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance().reference
        st = FirebaseStorage.getInstance().reference
        db.child("images").addListenerForSingleValueEvent(valueEvenListener)
    }

    fun updateImage(image: Image) {
        if (image.data != "") {
            val fileName = File(image.data)
            val imageName = fileName.getName()

            var imageRef = st.child(userId + '/' + imageName)
            val baos = ByteArrayOutputStream()
            val bitmap = readImageFromPath(context, image.data)

            bitmap?.let {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()
                val uploadTask = imageRef.putBytes(data)
                uploadTask.addOnFailureListener {
                    println(it.message)
                }.addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                        image.data = it.toString()
                        db.child("images").child(image.fbId).setValue(image)
                    }
                }
            }
        }
    }

    fun updateImage(image: Image, bitmap: Bitmap) {
        val imageName = bitmap.hashCode()

        var imageRef = st.child(userId + '/' + imageName)
        val baos = ByteArrayOutputStream()

        bitmap.let {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()
            val uploadTask = imageRef.putBytes(data)
            uploadTask.addOnFailureListener {
                println(it.message)
            }.addOnSuccessListener { taskSnapshot ->
                taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                    image.data = it.toString()
                    db.child("images").child(image.fbId).setValue(image)
                }
            }
        }
    }
}