package com.harera.remote.request

data class UpdateUsernameRequest(
    val oldUsername: String,
    val newUsername: String,
)
