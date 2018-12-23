package org.wit.archaeologicalfieldwork.helpers

import java.text.SimpleDateFormat
import java.util.*

fun simplifyDate(date: Date) : String{
    return SimpleDateFormat.getDateInstance().format(date).toString()
}