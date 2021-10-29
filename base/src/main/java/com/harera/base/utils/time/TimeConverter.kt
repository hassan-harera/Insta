package com.harera.base.utils.time

import androidx.room.TypeConverter
import com.google.firebase.Timestamp
import java.util.*

object TimeConverter {
    fun toDate(timestamp: Timestamp): Date =
        Date(timestamp.seconds)

    fun toTimestamp(date: Date): Timestamp =
        Timestamp(date)
}