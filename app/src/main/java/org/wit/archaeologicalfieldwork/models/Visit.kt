package org.wit.archaeologicalfieldwork.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Visit(
    var date: Date = Date(),
    var username: String = "",
    var hillfortId: Long = -1
    ) : Parcelable