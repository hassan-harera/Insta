package com.whiteside.insta.modelget

import com.google.firebase.Timestamp

class Message {
    lateinit var fromUid: String
    lateinit var toUid: String
    lateinit var message: String
    lateinit var time: Timestamp
}
