package com.harera.model.request.signup

data class SignupByEmailRequest(
    val token: String,
)


data class SignupByFacebookRequest(
    val token: String,
)


data class SignupByGoogleRequest(
    val token: String,
)
