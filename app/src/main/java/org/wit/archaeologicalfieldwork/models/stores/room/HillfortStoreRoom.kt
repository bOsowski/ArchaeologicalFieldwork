package org.wit.archaeologicalfieldwork.models.stores.room

import android.content.Context
import androidx.room.Room
import org.wit.archaeologicalfieldwork.models.Hillfort
import org.wit.archaeologicalfieldwork.models.stores.Store

class HillfortStoreRoom(val context: Context): Store<Hillfort>{
    var dao: HillfortDao

    init {
       val database = Room.databaseBuilder(context, Database::class.java, "room_sample.db")
           .fallbackToDestructiveMigration()
           .build()
        dao = database.hillfortDao()
    }

    override fun findAll(): List<Hillfort> {
        return dao.findAllHillforts()
    }

    override fun create(item: Hillfort) {
        dao.create(item)
    }

    override fun update(item: Hillfort) {
        dao.update(item)
    }

    override fun delete(item: Hillfort) {
       dao.delete(item)
    }

    fun clear(){

    }
}