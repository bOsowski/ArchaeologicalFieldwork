package org.wit.archaeologicalfieldwork.models.stores

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.wit.archaeologicalfieldwork.helpers.exists
import org.wit.archaeologicalfieldwork.helpers.read
import org.wit.archaeologicalfieldwork.helpers.write
import org.wit.archaeologicalfieldwork.models.User
import java.util.ArrayList

class UserJSONStore : UserMemStore{

    val JSON_FILE = "users.json"
    val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
    val listType = object : TypeToken<ArrayList<User>>() {}.type
    val context: Context

    constructor (context: Context) {
        this.context = context
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
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
        val jsonString = gsonBuilder.toJson(users, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        users = Gson().fromJson(jsonString, listType)
    }
}