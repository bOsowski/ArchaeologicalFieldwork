package org.wit.archaeologicalfieldwork.models.stores.room

import androidx.room.*
import org.w3c.dom.Node
import org.wit.archaeologicalfieldwork.models.Hillfort
import org.wit.archaeologicalfieldwork.models.Image
import org.wit.archaeologicalfieldwork.models.Note
import org.wit.archaeologicalfieldwork.models.Visit

@Dao
interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(image: Image): Long

    @Query("SELECT * FROM Image")
    fun findAll(): MutableList<Image>

    @Query("SELECT * FROM Image WHERE id = :id")
    fun find(id: Long): Image?

//    @Query("SELECT * FROM Image WHERE id = :id")
//    fun findImageById(id: Long): Image
//
//    @Query("SELECT * FROM Image WHERE hillfortId = :hillfortId")
//    fun findAllImages(hillfortId: Long): List<Image>
//
//    @Query("SELECT * FROM Note WHERE id = :id")
//    fun findNoteById(id: Long) : Node
//
//    @Query("SELECT * FROM Note WHERE hillfortId = :hillfortId")
//    fun findAllNotesForHillfort(hillfortId: Long): List<Note>
//
//    @Query("SELECT * FROM Visit WHERE id = :id")
//    fun findVisitById(id: Long): Note
//
//    @Query("SELECT * FROM Visit WHERE hillfortId = :hillfortId")
//    fun findAllVisitsForHillfort(hillfortId: Long): List<Visit>
//
//    @Query("SELECT * FROM Visit WHERE userId = :userId")
//    fun findAllUserVisits(userId: Long): List<Visit>

    @Update
    fun update(image: Image)

    @Delete
    fun delete(image: Image)

//    @Delete
//    fun delete(image: Image)
//
//    @Delete
//    fun delete(note: Note)
//
//    @Delete
//    fun delete(visit: Visit)
}