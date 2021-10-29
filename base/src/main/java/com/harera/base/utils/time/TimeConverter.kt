package com.harera.base.utils.time

import androidx.room.TypeConverter
import java.util.*

object TimeConverter  {
    @TypeConverter
    fun toDate(time: Long): Date =
        Date(time)

    @TypeConverter
    fun toTimestamp(date: Date): Long =
        date.time
}
