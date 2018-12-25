package org.wit.archaeologicalfieldwork.models

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(foreignKeys = [ForeignKey(entity = Hillfort::class, parentColumns = ["id"], childColumns = ["hillfortId"]),
                       ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["userId"])])
data class Visit(
    @Embedded var date: Date = Date(),
    var hillfortId: Long = 0,
    var userId: Long = 0,
    @PrimaryKey(autoGenerate = true) var id: Long = 0
    ) : Parcelable