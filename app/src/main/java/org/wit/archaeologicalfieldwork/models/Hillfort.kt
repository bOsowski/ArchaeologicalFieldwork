package org.wit.archaeologicalfieldwork.models

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity//(foreignKeys = [ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["addedBy"])])
data class Hillfort(
    var name: String = "",
    var description: String = "",
//    @Embedded  var location: Location = Location(),
    var addedBy: Long = 0,
    @PrimaryKey(autoGenerate = true) var id: Long = 0
) : Parcelable