package org.wit.archaeologicalfieldwork.models.stores.room

import androidx.room.*
import org.wit.archaeologicalfieldwork.models.Visit

@Dao
interface VisitDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(visit: Visit): Long

    @Query("SELECT * FROM Visit")
    fun findAll(): MutableList<Visit>

    @Query("SELECT * FROM Visit WHERE id = :id")
    fun find(id: Long): Visit?

//    @Query("SELECT * FROM Visit WHERE id = :id")
//    fun findVisitById(id: Long): Visit
//
//    @Query("SELECT * FROM Visit WHERE hillfortId = :hillfortId")
//    fun findAllVisits(hillfortId: Long): List<Visit>
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
    fun update(visit: Visit)

    @Delete
    fun delete(visit: Visit)

//    @Delete
//    fun delete(visit: Visit)
//
//    @Delete
//    fun delete(note: Note)
//
//    @Delete
//    fun delete(visit: Visit)
}