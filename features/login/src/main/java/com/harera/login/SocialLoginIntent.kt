package com.harera.login

import com.harera.base.state.BaseState

sealed class SocialLoginIntent : BaseState() {
    data class FacebookLogin(val token: String) : SocialLoginIntent()
    data class GoogleLogin(val token : String) : SocialLoginIntent()
}
