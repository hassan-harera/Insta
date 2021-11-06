package com.harera.model.model

import org.joda.time.DateTime


//TODO
data class Message(
    var receiver: String,
    var time: DateTime,
    var sender: String,
    var message: String,
)

