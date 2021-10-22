package com.harera.login

data class LoginState(
    var passwordError: Int? = null,
    var emailError: Int? = null,
    var isValid: Boolean = false,
)