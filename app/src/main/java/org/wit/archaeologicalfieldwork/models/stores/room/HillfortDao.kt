package org.wit.archaeologicalfieldwork.models.stores.room

import androidx.room.*
import org.wit.archaeologicalfieldwork.models.Hillfort

@Dao
interface HillfortDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun create(hillfort: Hillfort)

    @Query("SELECT * FROM Hillfort")
    fun findAll(): MutableList<Hillfort>

    @Query("SELECT * FROM Hillfort WHERE id = :id")
    fun find(id: Long): Hillfort

    @Update
    fun update(hillfort: Hillfort)

    @Delete
    fun delete(hillfort: Hillfort)
}