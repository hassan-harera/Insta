package com.harera.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.harera.base.base.BaseViewModel
import com.harera.base.datastore.LocalStore
import com.harera.base.state.LoginState
import com.harera.base.state.PostState
import com.harera.base.state.BaseState
import com.harera.base.utils.Validity
import com.harera.base.validity.LoginFormValidity
import com.harera.model.request.login.LoginByEmailRequest
import com.harera.model.response.Token
import com.harera.repository.AuthManager
import com.harera.repository.ProfileRepository
import kotlinx.coroutines.delay
import org.koin.core.context.loadKoinModules

class LoginViewModel constructor(
    private val authManager: AuthManager,
    private val profileRepository: ProfileRepository,
    userSharedPreferences: LocalStore,
) : BaseViewModel<LoginState>(userSharedPreferences) {

    private val TAG = "LoginViewModel"

    var emailState by mutableStateOf("")
        private set
    var passwordState by mutableStateOf("")
        private set

    private fun checkFormValidity() {
        if (emailState.isBlank()) {
            state = LoginState.FormValidity(LoginFormValidity(emailError = true))
        } else if (!Validity.checkEmail(emailState)) {
            state = LoginState.FormValidity(LoginFormValidity(emailError = true))
        } else if (passwordState.isBlank()) {
            state = LoginState.FormValidity(LoginFormValidity(passwordError = true))
        } else if (!Validity.checkPassword(passwordState)) {
            state = LoginState.FormValidity(LoginFormValidity(passwordError = true))
        } else {
            state = LoginState.FormValidity(LoginFormValidity(isValid = true))
        }
    }

    fun setEmail(it: String) {
        emailState = it
        checkFormValidity()
    }

    fun setPassword(it: String) {
        passwordState = it
        checkFormValidity()
    }

    suspend fun login() {
        state = BaseState.Loading()
        authManager
            .loginWithEmail(
                LoginByEmailRequest(
                    email = emailState,
                    password = passwordState,
                )
            )
            .onSuccess { token ->
                updateToken(token.token)
                delay(150)
                getUser(token)
            }
            .onFailure {
                state = PostState.Error(it.message)
                handleFailure(it)
            }
    }

    private suspend fun getUser(token: Token) {
        state = BaseState.Loading()
        profileRepository
            .getProfile(token.token)
            .onSuccess { user ->
                updateUsername(user.username)
                state = LoginState.LoginSuccess
            }
            .onFailure {
                state = PostState.Error(it.message)
                updateToken("")
                handleFailure(it)
            }
    }
}