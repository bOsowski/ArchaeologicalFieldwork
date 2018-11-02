package org.wit.archaeologicalfieldwork.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize class User(
    var id: Long = -1,
    var email: String = "",
    var password: String = ""
) : Parcelable