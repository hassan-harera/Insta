package com.harera.base.state

import com.harera.base.validity.LoginFormValidity

sealed class LoginState : BaseState() {
    data class FormValidity(val formValidity: LoginFormValidity) : LoginState()
    object LoginSuccess : LoginState()
}
