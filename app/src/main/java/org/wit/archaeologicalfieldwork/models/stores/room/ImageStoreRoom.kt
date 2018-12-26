package org.wit.archaeologicalfieldwork.models.stores.room

import android.content.Context
import androidx.room.Room
import org.jetbrains.anko.coroutines.experimental.bg
import org.wit.archaeologicalfieldwork.models.*
import org.wit.archaeologicalfieldwork.models.stores.Store

class ImageStoreRoom(val context: Context): Store<Image>{
    override suspend fun find(id: Long): Image {
        val defferedImage = bg{dao.find(id)}
        val image = defferedImage.await()
        return image
    }

    var dao: ImageDao

    init {
       val database = Room.databaseBuilder(context, Database::class.java, "room_sample.db")
           .fallbackToDestructiveMigration()
           .build()
        dao = database.imageDao()
    }

    override suspend fun findAll(): MutableList<Image> {
        val defferedImages = bg{dao.findAll()}
        val images = defferedImages.await()
        return images
    }

    override suspend fun create(item: Image): Long {
        val defferedIndex = bg{dao.create(item)}
        val index = defferedIndex.await()
        return index
    }

    override suspend fun update(item: Image) {
        bg{dao.update(item)}
    }

    override suspend fun delete(item: Image) {
        bg{dao.delete(item)}
    }

    fun clear(){

    }
}