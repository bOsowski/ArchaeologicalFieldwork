package org.wit.archaeologicalfieldwork.models.stores

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.archaeologicalfieldwork.helpers.exists
import org.wit.archaeologicalfieldwork.helpers.read
import org.wit.archaeologicalfieldwork.helpers.write
import org.wit.archaeologicalfieldwork.models.Data
import org.wit.archaeologicalfieldwork.models.Hillfort
import org.wit.archaeologicalfieldwork.models.User
import java.util.ArrayList

class UnifiedJSONStore: AnkoLogger {

    val JSON_FILE = "data.json"
    val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
    val context: Context
    lateinit var data: Data

    constructor (context: Context) {
        this.context = context
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
        else{
            data = Data()
        }
    }

    fun findAll(): Data {
        return data
    }


    fun delete(item: Hillfort) {
        data.hillforts.remove(item)
        serialize()
    }

    fun create(item: Hillfort) {
        var largestId: Long = -1
        data.hillforts.forEach{
            if(largestId < it.id){
                largestId = it.id
            }
        }
        item.id = largestId+1
        data.hillforts.add(item)
        serialize()
    }

    fun update(item: Hillfort) {
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
        serialize()
    }

    fun delete(item: User) {
        data.users.remove(item)
        serialize()
    }

    fun create(item: User) {
        var largestId: Long = -1
        data.users.forEach{
            if(largestId < it.id){
                largestId = it.id
            }
        }
        item.id = largestId+1
        data.users.add(item)
        serialize()
    }

    fun update(item: User) {
        val foundUser: User? = data.users.find { u -> u.id == item.id }
        if (foundUser != null) {
            foundUser.email = item.email
            foundUser.password = item.password
        }
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(data, Data::class.java)
        write(context, JSON_FILE, jsonString)
    }

    fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        data = Gson().fromJson(jsonString, Data::class.java)
    }
}