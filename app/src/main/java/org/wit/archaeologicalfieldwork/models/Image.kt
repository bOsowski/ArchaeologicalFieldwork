package org.wit.archaeologicalfieldwork.models

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(foreignKeys = [ForeignKey(entity = Hillfort::class, parentColumns = ["id"], childColumns = ["hillfortId"]),
    ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["addedBy"])])
data class Image (
//    @Embedded var data: Bitmap? = null, todo: fix this
    var hillfortId: Long = 0,
    var addedBy: Long = 0,
    @PrimaryKey(autoGenerate = true) var id: Long = 0
) : Parcelable