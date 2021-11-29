package com.harera.remote.repository

import com.google.gson.Gson
import com.harera.model.request.login.LoginByEmailRequest
import com.harera.model.request.login.LoginByFacebookRequest
import com.harera.model.request.login.LoginByGoogleRequest
import com.harera.model.request.signup.SignupByEmailRequest
import com.harera.model.request.signup.SignupByFacebookRequest
import com.harera.model.request.signup.SignupByGoogleRequest
import com.harera.model.response.Token
import com.harera.remote.service.AuthenticationService
import com.harera.repository.AuthManager

class AuthRepositoryImpl(
    private val authService: AuthenticationService,
) : AuthManager {

    override suspend fun loginWithEmail(request: LoginByEmailRequest): Result<Token> =
        kotlin.runCatching {
            authService.loginWithEmail(request)
        }

    override suspend fun loginWithFacebook(request: LoginByFacebookRequest): Result<String> {
        TODO("Not yet implemented")
    }

    override suspend fun loginWithFacebook(token: String) =
        runCatching<String> {
            authService.loginWithFacebook(token)
        }

    override suspend fun loginWithGoogle(request: LoginByGoogleRequest): Result<String> {
        TODO("Not yet implemented")
    }

    override suspend fun signupWithEmail(request: SignupByEmailRequest): Result<String> =
        runCatching<String> {
            authService.signupWithEmail(request)
        }

    override suspend fun signupWithFacebook(request: SignupByFacebookRequest): Result<String> {
        TODO("Not yet implemented")
    }

    override suspend fun signupWithGoogle(request: SignupByGoogleRequest): Result<String> {
        TODO("Not yet implemented")
    }

    override suspend fun signOut() {
        TODO("Not yet implemented")
    }
}