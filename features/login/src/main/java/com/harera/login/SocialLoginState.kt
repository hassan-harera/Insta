package com.harera.login

import com.harera.base.state.BaseState

sealed class SocialLoginState : BaseState() {
    object LoggedSuccessfully : SocialLoginState()
}
