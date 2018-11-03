package org.wit.archaeologicalfieldwork.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Hillfort(
    var id: Long = -1,
    var name: String = "",
    var description: String = "",
    var images: ArrayList<String> = ArrayList(),
    var location: Location = Location(),
    var addedBy: Long = -1,
    var notes: ArrayList<Note> = ArrayList(),
    var visits: ArrayList<Visit> = ArrayList()
) : Parcelable