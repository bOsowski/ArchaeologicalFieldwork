package org.wit.archaeologicalfieldwork.models.stores.room

import androidx.room.Database
import androidx.room.RoomDatabase
import org.wit.archaeologicalfieldwork.models.*

@Database(entities = [Hillfort::class, User::class, Visit::class, Note::class, Image::class], version = 1)
abstract class Database: RoomDatabase(){

   abstract fun hillfortDao(): HillfortDao
}