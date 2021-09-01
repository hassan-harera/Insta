package com.whiteside.insta.modelset

import com.google.firebase.Timestamp

data class Message(
    var from: String,
    var to: String,
    var message: String,
    var time: Timestamp,
)
