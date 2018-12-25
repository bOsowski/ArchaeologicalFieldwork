package org.wit.archaeologicalfieldwork.models.stores.room

import androidx.room.*
import org.w3c.dom.Node
import org.wit.archaeologicalfieldwork.models.*

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(user: User)

    @Query("SELECT * FROM User")
    fun findAll(): MutableList<User>

    @Query("SELECT * FROM User WHERE id = :id")
    fun find(id: Long): User

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
    fun update(user: User)

    @Delete
    fun delete(user: User)

//    @Delete
//    fun delete(image: Image)
//
//    @Delete
//    fun delete(note: Note)
//
//    @Delete
//    fun delete(visit: Visit)
}