package com.harera.remote.request

data class LoginByEmailRequest(
    val email: String,
    val password: String,
)