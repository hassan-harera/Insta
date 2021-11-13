package com.harera.notifications

import com.example.response.Notification
import com.harera.base.state.State

sealed class NotificationState : State() {

    data class NotificationsFetched(val notifications: List<Notification>) : NotificationState()

}
