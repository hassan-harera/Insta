package com.harera.base.validity

data class LoginFormValidity(
    var passwordError: Boolean = false,
    var nameError: Boolean = false,
    var emailError: Boolean = false,
    var isValid: Boolean = false,
)
