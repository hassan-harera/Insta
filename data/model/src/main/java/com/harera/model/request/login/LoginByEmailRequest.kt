package com.harera.model.request.login

import kotlinx.serialization.Serializable

@Serializable
data class LoginByEmailRequest(
    val email: String,
    val password: String,
)