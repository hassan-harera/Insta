package com.harera.repository

import com.harera.model.response.Notification

interface NotificationsRepository {

    suspend fun getNotifications(token: String, page : Int? = null, pageSize: Int?): Result<List<Any>>
}
