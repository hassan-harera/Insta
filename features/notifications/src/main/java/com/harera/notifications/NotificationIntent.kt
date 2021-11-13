package com.harera.notifications

import com.harera.base.state.State

sealed class NotificationIntent {
    object GetNotifications : NotificationIntent()
    object GetMoreNotifications : NotificationIntent()
}
