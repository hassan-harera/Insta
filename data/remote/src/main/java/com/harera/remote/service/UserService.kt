package com.harera.remote.service

import com.harera.model.model.User
import com.harera.model.response.Connection
import com.harera.remote.Routing
import com.harera.remote.URL
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

interface ProfileService {
    suspend fun getProfile(token: String): User
    suspend fun getConnections(token: String): List<Connection>
    suspend fun getUser(token: String, username: String): User
}

class ProfileServiceImpl(private val client: HttpClient) : ProfileService {

    override suspend fun getProfile(token: String): User =
        client.get<User> {
            url(Routing.GET_PROFILE)
            header(HttpHeaders.Authorization, "Bearer $token")
        }

    override suspend fun getConnections(token: String): List<Connection> =
        client.get<List<Connection>> {
            url(Routing.GET_CONNECTIONS)
            header(HttpHeaders.Authorization, "Bearer $token")
        }

    override suspend fun getUser(token: String, username: String): User  =
        client.get<User> {
            url(URL.BASE_URL.plus("/$username"))
            header(HttpHeaders.Authorization, "Bearer $token")
        }
}