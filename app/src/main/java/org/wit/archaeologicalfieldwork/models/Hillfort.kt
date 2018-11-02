package org.wit.archaeologicalfieldwork.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Hillfort(
    var name: String = "",
    var location: Location = Location()
) : Parcelable