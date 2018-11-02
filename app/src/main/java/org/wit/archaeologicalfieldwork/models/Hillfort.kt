package org.wit.archaeologicalfieldwork.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Hillfort(
    var id: Long = -1,
    var name: String = "",
    var description: String = "",
    var images: List<String> = ArrayList<String>(),
    var location: Location = Location(),
    var addedBy: String = "",
    var notes: List<String> = ArrayList<String>(),
    var visits: List<Visit> = ArrayList<Visit>()
) : Parcelable