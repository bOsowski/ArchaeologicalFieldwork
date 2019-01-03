package org.wit.archaeologicalfieldwork.helpers

import java.text.SimpleDateFormat
import java.util.*

val noteRefreshTime: Long = 5000

fun simplifyDate(date: Date) : String{
    return SimpleDateFormat.getDateInstance().format(date).toString()
}