package org.wit.archaeologicalfieldwork.models.stores.room

import android.content.Context
import androidx.room.Room
import org.jetbrains.anko.coroutines.experimental.bg
import org.wit.archaeologicalfieldwork.models.*
import org.wit.archaeologicalfieldwork.models.stores.Store

class UserStoreRoom(val context: Context): Store<User>{
    override suspend fun find(id: Long): User {
        val defferedUser = bg{ dao.find(id)}
        val user = defferedUser.await()
        return user
    }

    var dao: UserDao

    init {
       val database = Room.databaseBuilder(context, Database::class.java, "room_sample.db")
           .fallbackToDestructiveMigration()
           .build()
        dao = database.userDao()
    }

    override suspend fun findAll(): MutableList<User> {
        val defferedUsers = bg{ dao.findAll()}
        val users = defferedUsers.await()
        return users
    }

    override suspend fun create(item: User) {
        bg{dao.create(item)}
    }

    override suspend fun update(item: User) {
        bg{dao.update(item)}
    }

    override suspend fun delete(item: User) {
        bg{ dao.delete(item)}
    }

    fun clear(){

    }
}