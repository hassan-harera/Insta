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
import com.harera.model.request.signup.SignupByEmailRequest
import com.harera.model.response.Token
import com.harera.repository.AuthManager
import com.harera.repository.ProfileRepository

class SignupViewModel constructor(
    private val authManager: AuthManager,
    private val profileRepository: ProfileRepository,
    userSharedPreferences: LocalStore,
) : BaseViewModel<LoginState>(userSharedPreferences) {

    private val TAG = "LoginViewModel"

    var emailState by mutableStateOf("")
        private set
    var passwordState by mutableStateOf("")
        private set
    var nameState by mutableStateOf("")
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
        } else if (nameState.isBlank()) {
            state = LoginState.FormValidity(LoginFormValidity(nameError = true))
        } else if (!Validity.checkName(nameState)) {
            state = LoginState.FormValidity(LoginFormValidity(nameError = true))
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

    fun setName(it: String) {
        nameState = it
        checkFormValidity()
    }

    suspend fun signup() {
        state = BaseState.Loading()
        authManager
            .signupWithEmail(
                SignupByEmailRequest(
                    email = emailState,
                    password = passwordState,
                    name = nameState
                )
            )
            .onSuccess {
                login()
            }
            .onFailure {
                state = PostState.Error(it.message)
                handleFailure(it)
            }
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
                updateToken(token.token)
                updateUsername(user.username)
                state = LoginState.LoginSuccess
            }
            .onFailure {
                state = PostState.Error(it.message)
                handleFailure(it)
            }
    }
}