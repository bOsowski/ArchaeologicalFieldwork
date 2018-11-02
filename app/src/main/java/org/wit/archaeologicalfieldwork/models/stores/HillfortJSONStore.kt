package org.wit.archaeologicalfieldwork.models.stores

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.wit.archaeologicalfieldwork.helpers.exists
import org.wit.archaeologicalfieldwork.helpers.read
import org.wit.archaeologicalfieldwork.helpers.write
import org.wit.archaeologicalfieldwork.models.Hillfort
import java.util.ArrayList

class HillfortJSONStore : HillfortMemStore{

    val JSON_FILE = "hillforts.json"
    val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
    val listType = object : TypeToken<ArrayList<Hillfort>>() {}.type
    val context: Context

    constructor (context: Context) {
        this.context = context
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun delete(item: Hillfort) {
        super.delete(item)
        serialize()
    }

    override fun create(item: Hillfort) {
        super.create(item)
        serialize()
    }


    override fun update(item: Hillfort) {
        super.update(item)
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(hillforts, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        hillforts = Gson().fromJson(jsonString, listType)
    }
}