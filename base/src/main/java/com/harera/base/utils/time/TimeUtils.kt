package com.harera.base.utils.time

import android.util.Log
import com.google.firebase.Timestamp
import java.util.*

class TimeUtils {

    companion object {
        private const val TAG = "TimeUtils"

        fun timeFromNow(timestamp: Timestamp): String {
            val seconds = Timestamp.now().seconds - timestamp.seconds
            return when {
                seconds >= yearSeconds -> {
                    val years = seconds / yearSeconds
                    "$years $YEAR_STRING"
                }
                seconds >= monthSeconds -> {
                    val months = seconds / monthSeconds
                    "$months $MONTH_STRING"
                }
                seconds >= weekSeconds -> {
                    val weeks = seconds / weekSeconds
                    "$weeks $WEEKS_STRING"
                }
                seconds >= daySeconds -> {
                    val days = seconds / daySeconds
                    "$days $DAYS_STRING"
                }
                seconds >= hourSeconds -> {
                    val hours = seconds / hourSeconds
                    "$hours $HOURS_STRING"
                }
                seconds >= minuteSeconds -> {
                    val minutes = seconds / minuteSeconds
                    "$minutes $MINUTES_STRING"
                }
                else -> {
                    SECONDS_STRING
                }
            }
        }

        fun timeFromNow(date: Date): String {
            val seconds = (date.seconds - Date().seconds) * 1000
            Log.d(TAG, "$seconds")

            return when {
                seconds >= yearSeconds -> {
                    val years = seconds / yearSeconds
                    "$years $YEAR_STRING"
                }
                seconds >= monthSeconds -> {
                    val months = seconds / monthSeconds
                    "$months $MONTH_STRING"
                }
                seconds >= weekSeconds -> {
                    val weeks = seconds / weekSeconds
                    "$weeks $WEEKS_STRING"
                }
                seconds >= daySeconds -> {
                    val days = seconds / daySeconds
                    "$days $DAYS_STRING"
                }
                seconds >= hourSeconds -> {
                    val hours = seconds / hourSeconds
                    "$hours $HOURS_STRING"
                }
                seconds >= minuteSeconds -> {
                    val minutes = seconds / minuteSeconds
                    "$minutes $MINUTES_STRING"
                }
                else -> {
                    SECONDS_STRING
                }
            }
        }

        private const val minuteSeconds = 60
        private const val hourSeconds = 3600
        private const val daySeconds = 86400
        private const val weekSeconds = 604800
        private const val monthSeconds = 2592000
        private const val yearSeconds = 31536000

        private const val SECONDS_STRING  = "secs ago"
        private const val MINUTES_STRING = "m"
        private const val HOURS_STRING = "h"
        private const val DAYS_STRING   = "d"
        private const val WEEKS_STRING = "w"
        private const val MONTH_STRING = "m"
        private const val YEAR_STRING = "y"
    }
}
