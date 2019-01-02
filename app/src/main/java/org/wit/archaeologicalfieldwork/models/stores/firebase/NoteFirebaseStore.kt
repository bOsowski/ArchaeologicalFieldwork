package org.wit.archaeologicalfieldwork.models.stores.firebase

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.jetbrains.anko.AnkoLogger
import org.wit.archaeologicalfieldwork.models.Note
import org.wit.archaeologicalfieldwork.models.stores.Store

class NoteFirebaseStore(val context: Context) : Store<Note>, AnkoLogger {

    val notes = ArrayList<Note>()
    lateinit var db: DatabaseReference

    override suspend fun findAll(): List<Note> {
        return notes
    }

    override suspend fun find(id: Long): Note? {
        return notes.find { it.id == id }
    }

    override suspend fun create(item: Note): Long {
        val key = db.child("notes").push().key
        item.fbId = key!!
        item.id = key.hashCode().toLong()
        notes.add(item)
        db.child("notes").child(key).setValue(item)
        return item.id
    }

    override suspend fun update(item: Note) {
        var foundNote: Note? = notes.find { it.fbId == item.fbId }
        if(foundNote != null){
            foundNote.text = item.text
            foundNote.lastEdited = item.lastEdited
        }
        db.child("notes").child(item.fbId).setValue(foundNote)
    }

    override suspend fun delete(item: Note) {
        db.child("notes").child(item.fbId).removeValue()
        notes.remove(item)
    }

    fun fetchNotes(notesReady: () -> Unit){
        val valueEvenListener = object : ValueEventListener{
            override fun onCancelled(error: DatabaseError){
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                notes.clear()
                dataSnapshot.children.mapNotNullTo(notes) {it.getValue<Note>(Note::class.java)}
                notesReady()
            }
        }
        db = FirebaseDatabase.getInstance().reference
        db.child("notes").addListenerForSingleValueEvent(valueEvenListener)
    }
}