package Model.Notifications

import com.google.firebase.Timestamp

open class Notification : Comparable<Notification> {
    var date: Timestamp? = null
    var type = 0

    override fun compareTo(o: Notification): Int {
        return o.date!!.compareTo(date!!)
    }
}