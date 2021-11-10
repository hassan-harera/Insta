package com.harera.model.response

import kotlinx.serialization.Serializable

@Serializable
data class MessageResponse(
    val message: String,
    var messageId: Int,
    var sender: String,
    var receiver: String,
    var time: String,
)