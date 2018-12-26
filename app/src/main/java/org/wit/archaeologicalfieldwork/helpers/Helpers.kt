package org.wit.archaeologicalfieldwork.helpers

import java.text.SimpleDateFormat
import java.util.*

val imageRefreshTime: Long = 30000
val noteRefreshTime: Long = 5000
val hillfortRefreshTime: Long = 10000

fun simplifyDate(date: Date) : String{
    return SimpleDateFormat.getDateInstance().format(date).toString()
}