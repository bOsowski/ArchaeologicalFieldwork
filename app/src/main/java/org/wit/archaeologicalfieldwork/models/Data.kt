package org.wit.archaeologicalfieldwork.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Data(
    var hillforts: ArrayList<Hillfort> = ArrayList()
) : Parcelable