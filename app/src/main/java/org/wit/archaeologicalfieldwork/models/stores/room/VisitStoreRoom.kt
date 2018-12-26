package org.wit.archaeologicalfieldwork.models.stores.room

import android.content.Context
import androidx.room.Room
import org.jetbrains.anko.coroutines.experimental.bg
import org.wit.archaeologicalfieldwork.models.*
import org.wit.archaeologicalfieldwork.models.stores.Store

class VisitStoreRoom(val context: Context): Store<Visit>{
    override suspend fun find(id: Long): Visit {
        val defferedVisit = bg{ dao.find(id) }
        val visit = defferedVisit.await()
        return visit
    }

    var dao: VisitDao

    init {
       val database = Room.databaseBuilder(context, Database::class.java, "room_sample.db")
           .fallbackToDestructiveMigration()
           .build()
        dao = database.visitDao()
    }

    override suspend fun findAll(): MutableList<Visit> {
        val defferedVisits = bg{ dao.findAll() }
        val visits = defferedVisits.await()
        return visits
    }

    override suspend fun create(item: Visit): Long {
        val defferedIndex = bg{dao.create(item)}
        val index = defferedIndex.await()
        return index
    }

    override suspend fun update(item: Visit) {
       bg{ dao.update(item) }
    }

    override suspend fun delete(item: Visit) {
       bg{ dao.delete(item) }
    }

    fun clear(){

    }
}