package com.harera.model.request.login

data class LoginByEmailRequest(
    val email: String,
    val password: String,
)