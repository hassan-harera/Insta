package com.harera.model.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class MessageResponse(
    val message: String,
    @PrimaryKey
    var messageId: Int,
    var sender: String,
    var receiver: String,
    var time: String,
)