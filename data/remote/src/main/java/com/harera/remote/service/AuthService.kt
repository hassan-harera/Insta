package com.harera.remote.service

import com.google.gson.Gson
import com.harera.remote.LoginMethods.EMAIL
import com.harera.remote.Parameters.Login_Method
import com.harera.remote.Parameters.Signup_Method
import com.harera.remote.Routing
import com.harera.model.request.login.LoginByEmailRequest
import com.harera.model.request.signup.SignupByEmailRequest
import com.harera.model.response.Token
import com.harera.remote.URL.BASE_URL
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.ContentType.Application.Json


interface AuthService {
    suspend fun loginWithEmail(request: LoginByEmailRequest): Token
    suspend fun signupWithEmail(request: SignupByEmailRequest): String
}

class AuthServiceImpl(
    private val client: HttpClient,
) : AuthService {

    override suspend fun signupWithEmail(
        request: SignupByEmailRequest,
    ) = client.post<String> {
        url(BASE_URL.plus(Routing.SIGNUP))
        parameter(Signup_Method, EMAIL)
        contentType(Json)
        body = request
    }

    override suspend fun loginWithEmail(request: LoginByEmailRequest) =
        client.post<Token> {
            url(BASE_URL.plus(Routing.LOGIN))
            parameter(Login_Method, EMAIL)
            contentType(Json)
            body = request
        }
}