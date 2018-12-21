package org.wit.archaeologicalfieldwork.models.stores

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.archaeologicalfieldwork.models.Data
import org.wit.archaeologicalfieldwork.models.Hillfort
import org.wit.archaeologicalfieldwork.models.User

abstract class UnifiedMemStore: AnkoLogger{

    var data: Data = Data()

    fun findAll(): Data {
        return data
    }

    open fun create(item: User) {
        var largestId: Long = -1
        data.users.forEach {
            if (largestId < it.id) {
                largestId = it.id
            }
        }
        item.id = largestId + 1
        data.users.add(item)
    }

    open fun create(item: Hillfort){
        var largestId: Long = -1
        data.hillforts.forEach{
            if(largestId < it.id){
                largestId = it.id
            }
        }
        item.id = largestId+1
        data.hillforts.add(item)
    }

    open fun update(item: User){
        val foundUser: User? = data.users.find { u -> u.id == item.id }
        if (foundUser != null) {
            foundUser.email = item.email
            foundUser.password = item.password
        }
    }


    open fun delete(item: User){
        data.users.remove(item)
    }

    open fun update(item: Hillfort){
        val foundHillfort: Hillfort? = data.hillforts.find { u -> u.id == item.id }
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

    open fun delete(item: Hillfort){
        data.hillforts.remove(item)
    }
}