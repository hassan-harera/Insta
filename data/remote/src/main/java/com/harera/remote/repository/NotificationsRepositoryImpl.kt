package com.harera.remote.repository

import com.example.response.Notification
import com.harera.remote.service.NotificationsService
import com.harera.repository.NotificationsRepository

class NotificationsRepositoryImpl(
    private val notificationsService: NotificationsService,
) : NotificationsRepository {

    override suspend fun getNotifications(
        token: String, page: Int?,
        pageSize: Int?,
    ): Result<List<Notification>> = kotlin.runCatching {
        notificationsService.getNotifications(token = token, page = page, pageSize = pageSize)
    }
}
