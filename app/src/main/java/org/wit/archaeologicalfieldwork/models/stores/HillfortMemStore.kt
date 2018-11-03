package org.wit.archaeologicalfieldwork.models.stores

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.archaeologicalfieldwork.models.Hillfort

open class HillfortMemStore : Store<Hillfort>, AnkoLogger{

    var hillforts = mutableListOf<Hillfort>()

    override fun findAll(): List<Hillfort> {
        return hillforts
    }

    override fun create(item: Hillfort) {
        item.id = getId()
        hillforts.add(item)
    }

    override fun update(item: Hillfort) {
        val foundHillfort: Hillfort? = hillforts.find { u -> u.id == item.id }
        if (foundHillfort != null) {
            foundHillfort.description = item.description
            foundHillfort.images = item.images
            foundHillfort.location = item.location
            foundHillfort.name = item.name
            foundHillfort.notes = item.notes
            foundHillfort.visits = item.visits
            info("Updated hillfort = ${foundHillfort}")
        }
    }

    override fun delete(item: Hillfort) {
        hillforts.remove(item)
    }

    fun getId() : Long{
        var largestId: Long = -1
        hillforts.forEach{
            if(largestId < it.id){
                largestId = it.id
            }
        }
        return largestId+1
    }
}