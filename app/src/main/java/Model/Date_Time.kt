package Model

import com.google.firebase.Timestamp

object Date_Time {
    @JvmStatic
    fun timeFromNow(timestamp: Timestamp): String {
        val seconds = Timestamp.now().seconds - timestamp.seconds
        return if (seconds >= 31557600) {
            val years = seconds / 31557600
            "$years years ago"
        } else if (seconds >= 2592000) {
            val months = seconds / 2592000
            "$months months ago"
        } else if (seconds >= 86400) {
            val days = seconds / 86400
            "$days days ago"
        } else if (seconds >= 3600) {
            val hours = seconds / 3600
            "$hours hours ago"
        } else if (seconds >= 60) {
            val minutes = seconds / 60
            "$minutes minutes ago"
        } else {
            "seconds ago"
        }
    }
}