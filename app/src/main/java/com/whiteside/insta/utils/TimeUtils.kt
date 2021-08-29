package com.whiteside.insta.utils

import com.google.firebase.Timestamp

class TimeUtils {

    companion object {
        fun timeFromNow(timestamp: Timestamp): String {
            val seconds = Timestamp.now().seconds - timestamp.seconds
            return when {
                seconds >= yearSeconds -> {
                    val years = seconds / yearSeconds
                    "$years years ago"
                }
                seconds >= monthSeconds -> {
                    val months = seconds / monthSeconds
                    "$months months ago"
                }
                seconds >= weekSeconds -> {
                    val weeks = seconds / weekSeconds
                    "$weeks weeks ago"
                }
                seconds >= daySeconds -> {
                    val days = seconds / daySeconds
                    "$days days ago"
                }
                seconds >= hourSeconds -> {
                    val hours = seconds / hourSeconds
                    "$hours hours ago"
                }
                seconds >= minuteSeconds -> {
                    val minutes = seconds / minuteSeconds
                    "$minutes minutes ago"
                }
                else -> {
                    "seconds ago"
                }
            }
        }

        private const val minuteSeconds = 60
        private const val hourSeconds = 3600
        private const val daySeconds = 86400
        private const val weekSeconds = 604800
        private const val monthSeconds = 2592000
        private const val yearSeconds = 31536000
    }
}
