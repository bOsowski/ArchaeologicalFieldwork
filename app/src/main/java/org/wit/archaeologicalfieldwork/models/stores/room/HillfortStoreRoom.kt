package org.wit.archaeologicalfieldwork.models.stores.room

import android.content.Context
import androidx.room.Room
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.coroutines.experimental.bg
import org.jetbrains.anko.info
import org.wit.archaeologicalfieldwork.models.*
import org.wit.archaeologicalfieldwork.models.stores.Store

class HillfortStoreRoom(val context: Context): Store<Hillfort>, AnkoLogger{
    override suspend fun find(id: Long): Hillfort {
        val defferedHillfort =  bg{  dao.find(id) }
        val hillfort = defferedHillfort.await()
        return hillfort
    }

    var dao: HillfortDao

    init {
       val database = Room.databaseBuilder(context, Database::class.java, "room_sample.db")
           .fallbackToDestructiveMigration()
           .build()
        dao = database.hillfortDao()
    }

    override suspend fun findAll(): MutableList<Hillfort> {
        val defferedHillforts = bg{dao.findAll()}
        val hillforts = defferedHillforts.await()
        return hillforts
    }

    override suspend fun create(item: Hillfort): Long {
        val defferedIndex = bg{dao.create(item)}
        val index = defferedIndex.await()
        return index
    }

    override suspend fun update(item: Hillfort) {
       bg{dao.update(item)}
    }

    override suspend fun delete(item: Hillfort) {
       bg{dao.delete(item)}
    }

    fun clear(){

    }
}