package com.harera.model.request.signup

import kotlinx.serialization.Serializable


data class SignupByFacebookRequest(
    val token: String,
)


data class SignupByGoogleRequest(
    val token: String,
)

@Serializable
data class SignupByEmailRequest(
    val email: String,
    val name: String,
    val password: String,
)