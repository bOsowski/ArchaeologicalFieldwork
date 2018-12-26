package org.wit.archaeologicalfieldwork.models.stores.room

import androidx.room.*
import org.wit.archaeologicalfieldwork.models.Note

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(note: Note): Long

    @Query("SELECT * FROM Note")
    fun findAll(): MutableList<Note>

    @Query("SELECT * FROM Note WHERE id = :id")
    fun find(id: Long): Note?

    @Update
    fun update(note: Note)

    @Delete
    fun delete(note: Note)
}