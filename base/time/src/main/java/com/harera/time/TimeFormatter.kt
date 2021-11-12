package com.harera.time

import org.joda.time.DateTime

object TimeFormatter {
    private const val TIME_PATTERN = "yyyy.MMM.dd GGG hh:mm aaa"

    fun stringToDateTime(time: String): DateTime =
        DateTime.parse(time)

    fun dateTimeToString(time: DateTime): String =
        time.toString()
}