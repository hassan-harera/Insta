package com.harera.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class MessageInsertRequest(
    val receiver: String,
    val message: String,
)