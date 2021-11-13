package com.harera.model.model

import androidx.room.Entity
import org.joda.time.DateTime


data class Message(
    var receiver: String,
    var time: DateTime,
    var sender: String,
    var message: String,
)

