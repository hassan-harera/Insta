package com.harera.repository

import com.harera.model.request.login.LoginByEmailRequest
import com.harera.model.request.login.LoginByFacebookRequest
import com.harera.model.request.login.LoginByGoogleRequest
import com.harera.model.request.signup.SignupByEmailRequest
import com.harera.model.request.signup.SignupByFacebookRequest
import com.harera.model.request.signup.SignupByGoogleRequest
import com.harera.model.response.Token

interface AuthManager {

    suspend fun loginWithEmail(request: LoginByEmailRequest): Result<Token>
    suspend fun loginWithFacebook(request: LoginByFacebookRequest): Result<String>
    suspend fun loginWithGoogle(request: LoginByGoogleRequest): Result<String>

    suspend fun signupWithEmail(request: SignupByEmailRequest): Result<String>
    suspend fun signupWithFacebook(request: SignupByFacebookRequest): Result<String>
    suspend fun signupWithGoogle(request: SignupByGoogleRequest): Result<String>

    suspend fun signOut()
}