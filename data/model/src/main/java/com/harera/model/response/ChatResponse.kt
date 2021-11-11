package com.harera.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ChatResponse(
    val username: String,
    var name: String,
    var userImageUrl: String? = null,
    var lastMessage: String,
    var time: String,
)