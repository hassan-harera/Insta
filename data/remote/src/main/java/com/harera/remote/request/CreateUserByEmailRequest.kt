package com.harera.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserByEmailRequest(
    val email: String,
    val name: String,
    val password: String,
)