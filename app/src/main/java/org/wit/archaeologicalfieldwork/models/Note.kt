package org.wit.archaeologicalfieldwork.models

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import org.wit.archaeologicalfieldwork.helpers.simplifyDate
import java.util.*

@Parcelize
@Entity(foreignKeys = [ForeignKey(entity = Hillfort::class, parentColumns = ["id"], childColumns = ["hillfortId"])])
data class Note(
    var addedBy: String = "",
    var hillfortId: Long = 0,
    var text: String = "",
    var creationDate: Long = Date().time,
    var lastEdited: Long = Date().time,
    @PrimaryKey(autoGenerate = true) var id: Long = 0
) : Parcelable