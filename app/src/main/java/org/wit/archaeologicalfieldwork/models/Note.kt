package org.wit.archaeologicalfieldwork.models

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(foreignKeys = [ForeignKey(entity = Hillfort::class, parentColumns = ["id"], childColumns = ["hillfortId"])])
data class Note(
    var addedBy: String = "",
    var hillfortId: Long = 0,
    var text: String = "",
    @Embedded var creationDate: Date = Date(),
    @Embedded var lastEdited: Date = Date(),
    @PrimaryKey(autoGenerate = true) var id: Long = 0
) : Parcelable