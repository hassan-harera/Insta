package com.harera.base.state

import com.harera.base.utils.Validity
import com.harera.base.validity.LoginFormValidity

sealed class LoginState : State() {
    data class FormValidity(val formValidity: LoginFormValidity) : LoginState()
    object LoginSuccess : LoginState()
}
