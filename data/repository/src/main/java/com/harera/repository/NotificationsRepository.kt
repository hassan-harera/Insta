package com.harera.repository

import com.example.response.Notification

interface NotificationsRepository {

    suspend fun getNotifications(token: String, page : Int? = null, pageSize: Int?): Result<List<Notification>>
}
