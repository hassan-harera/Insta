package com.harera.login

import androidx.lifecycle.viewModelScope
import com.harera.base.base.BaseViewModel
import com.harera.base.datastore.LocalStore
import com.harera.base.state.BaseState
import com.harera.repository.AuthManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class SocialLoginViewModel(
    localStore: LocalStore,
    private val authManager: AuthManager,
) : BaseViewModel<SocialLoginState>(localStore) {

    val intent = Channel<SocialLoginIntent>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            triggerIntent()
        }
    }

    private suspend fun triggerIntent() {
        intent.consumeAsFlow().collect {
            when (it) {
                is SocialLoginIntent.FacebookLogin -> {
                    facebookLogin(it.token)
                }

                is SocialLoginIntent.GoogleLogin -> {
                    googleLogin(it.token)
                }
            }
        }
    }

    private fun googleLogin(token: String) {
        TODO("Not yet implemented")
    }


    private suspend fun facebookLogin(token: String) {
        state = BaseState.Loading()
        authManager
            .loginWithFacebook(token)
            .onFailure {
                it.printStackTrace()
            }
            .onSuccess { token ->
                updateToken(token)
                state = SocialLoginState.LoggedSuccessfully
            }
    }
}