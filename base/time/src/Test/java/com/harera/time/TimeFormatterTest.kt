package com.harera.time

import org.joda.time.DateTime
import org.junit.Test

class TimeFormatterTest {

    @Test
    fun stringToDateTime() {
        println(TimeFormatter.stringToDateTime("02021.November.03 AD 08:01 PM"))
        println(DateTime.now())
    }

    @Test
    fun dateTimeToString() {
    }
}