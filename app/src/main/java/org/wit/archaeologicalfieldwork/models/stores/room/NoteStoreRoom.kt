package org.wit.archaeologicalfieldwork.models.stores.room

import android.content.Context
import androidx.room.Room
import org.jetbrains.anko.coroutines.experimental.bg
import org.wit.archaeologicalfieldwork.models.*
import org.wit.archaeologicalfieldwork.models.stores.Store

class NoteStoreRoom(val context: Context): Store<Note>{
    override suspend fun find(id: Long): Note {
        val defferedNote = bg{dao.find(id)}
        val note = defferedNote.await()
        return note
    }

    var dao: NoteDao

    init {
       val database = Room.databaseBuilder(context, Database::class.java, "room_sample.db")
           .fallbackToDestructiveMigration()
           .build()
        dao = database.noteDao()
    }

    override suspend fun findAll(): MutableList<Note> {
        val defferedNotes = bg{dao.findAll()}
        val notes = defferedNotes.await()
        return notes
    }

    override suspend fun create(item: Note): Long {
        val defferedIndex = bg{dao.create(item)}
        val index = defferedIndex.await()
        return index
    }

    override suspend fun update(item: Note) {
        bg{dao.update(item)}
    }

    override suspend fun delete(item: Note) {
        bg{ dao.delete(item) }
    }

    fun clear(){

    }
}