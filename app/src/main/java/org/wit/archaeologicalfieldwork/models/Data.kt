package org.wit.archaeologicalfieldwork.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Data(
    var users: ArrayList<User>,
    var hillforts: ArrayList<Hillfort>
) : Parcelable