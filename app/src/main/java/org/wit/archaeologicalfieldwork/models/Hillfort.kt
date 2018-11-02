package org.wit.archaeologicalfieldwork.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Hillfort(
    var id: Long = -1,
    var name: String = "",
    var description: String = "",
    var images: ArrayList<String> = ArrayList<String>(),
    var location: Location = Location(),
    var addedBy: String = "",
    var notes: ArrayList<String> = ArrayList<String>(),
    var visits: ArrayList<Visit> = ArrayList<Visit>()
) : Parcelable