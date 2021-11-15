package com.harera.notifications

sealed class NotificationIntent {
    object GetNotifications : NotificationIntent()
    object GetMoreNotifications : NotificationIntent()
}
