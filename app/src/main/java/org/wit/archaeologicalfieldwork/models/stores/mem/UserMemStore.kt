//package org.wit.archaeologicalfieldwork.models.stores.mem
//
//import org.wit.archaeologicalfieldwork.models.User
//import org.wit.archaeologicalfieldwork.models.stores.Store
//
//open class UserMemStore : Store<User> {
//
//    var users = mutableListOf<User>()
//
//    override fun findAll(): List<User> {
//        return users
//    }
//
//    override fun create(item: User) {
//        item.id = getId()
//        users.add(item)
//    }
//
//    override fun update(item: User) {
//        val foundUser: User? = users.find { u -> u.id == item.id }
//        if (foundUser != null) {
//            foundUser.email = item.email
//            foundUser.password = item.password
//        }
//    }
//
//    override fun delete(item: User) {
//        users.remove(item)
//    }
//
//    fun getId() : Long{
//        var largestId: Long = -1
//        users.forEach{
//            if(largestId < it.id){
//               largestId = it.id
//            }
//        }
//        return largestId+1
//    }
//}