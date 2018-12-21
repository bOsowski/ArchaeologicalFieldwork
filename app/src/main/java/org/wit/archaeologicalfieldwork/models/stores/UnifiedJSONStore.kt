package org.wit.archaeologicalfieldwork.models.stores

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.jetbrains.anko.AnkoLogger
import org.wit.archaeologicalfieldwork.helpers.exists
import org.wit.archaeologicalfieldwork.helpers.read
import org.wit.archaeologicalfieldwork.helpers.write
import org.wit.archaeologicalfieldwork.models.Data
import org.wit.archaeologicalfieldwork.models.Hillfort
import org.wit.archaeologicalfieldwork.models.User

class UnifiedJSONStore(val context: Context) : UnifiedMemStore(), AnkoLogger {

    val JSON_FILE = "data.json"
    val gsonBuilder = GsonBuilder().setPrettyPrinting().create()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
        else{
            data = Data()
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

    override fun delete(item: User) {
        super.delete(item)
        serialize()
    }

    override fun create(item: User) {
        super.create(item)
        serialize()
    }

    override fun update(item: User) {
        super.update(item)
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(data, Data::class.java)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        println(jsonString)
        data = Gson().fromJson(jsonString, Data::class.java)
    }
}