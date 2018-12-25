package org.wit.archaeologicalfieldwork.models.stores

interface Store<T>{
    suspend fun findAll(): List<T>
    suspend fun find(id: Long): T
    suspend fun create(item: T)
    suspend fun update(item: T)
    suspend fun delete(item: T)
}