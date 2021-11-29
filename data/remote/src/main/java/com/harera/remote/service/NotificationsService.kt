package com.harera.remote.service

import com.harera.model.response.Notification
import com.harera.remote.Routing
import com.harera.remote.URL
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

interface NotificationsService {

    suspend fun getNotifications(
        token: String,
        page: Int? = null,
        pageSize: Int? = null,
    ): List<Notification>
}

class NotificationsServiceImpl(private val client: HttpClient) : NotificationsService {

    override suspend fun getNotifications(
        token: String,
        page: Int?,
        pageSize: Int?,
    ): List<Notification> =
        client.get(
            URL.BASE_URL.plus(Routing.NOTIFICATIONS)
        ) {
        }
}
