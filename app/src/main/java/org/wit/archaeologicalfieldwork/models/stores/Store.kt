package org.wit.archaeologicalfieldwork.models.stores

interface Store<T>{
    fun findAll(): List<T>
    fun create(item: T)
    fun update(item: T)
    fun delete(item: T)
}