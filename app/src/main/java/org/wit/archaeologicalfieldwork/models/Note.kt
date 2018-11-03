package org.wit.archaeologicalfieldwork.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Note(var userId: Long = -1,
                var text: String = "",
                var creationDate: Date = Date(),
                var lastEdited: Date = Date()
) : Parcelable