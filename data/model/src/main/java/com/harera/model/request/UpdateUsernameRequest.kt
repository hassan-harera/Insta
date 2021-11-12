package com.harera.model.request

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUsernameRequest(
    val oldUsername: String,
    val newUsername: String,
)
