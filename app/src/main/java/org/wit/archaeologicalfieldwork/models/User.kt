package org.wit.archaeologicalfieldwork.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
class User(
    var email: String = "",
    var password: String = "",
    @PrimaryKey(autoGenerate = true) var id: Long = 0
) : Parcelable