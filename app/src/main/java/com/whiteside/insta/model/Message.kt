package com.whiteside.insta.model

import java.util.*

class Message {
    var from: String? = null
    var to: String? = null
    var message: String? = null
    var seconds: Long = 0
    override fun hashCode(): Int {
        return Objects.hash(from, to, message, seconds)
    }
}
