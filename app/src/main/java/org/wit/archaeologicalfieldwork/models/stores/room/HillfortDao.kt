package org.wit.archaeologicalfieldwork.models.stores.room

import androidx.room.*
import org.w3c.dom.Node
import org.wit.archaeologicalfieldwork.models.Hillfort
import org.wit.archaeologicalfieldwork.models.Image
import org.wit.archaeologicalfieldwork.models.Note
import org.wit.archaeologicalfieldwork.models.Visit

@Dao
interface HillfortDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(hillfort: Hillfort)

    @Query("SELECT * FROM Hillfort")
    fun findAllHillforts(): List<Hillfort>

    @Query("SELECT * FROM Hillfort WHERE id = :id")
    fun findHillfortById(id: Long): Hillfort

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
    fun update(hillfort: Hillfort)

    @Delete
    fun delete(hillfort: Hillfort)

//    @Delete
//    fun delete(image: Image)
//
//    @Delete
//    fun delete(note: Note)
//
//    @Delete
//    fun delete(visit: Visit)
}