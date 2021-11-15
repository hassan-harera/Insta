package com.harera.notifications

import com.harera.model.response.Notification
import com.harera.base.state.BaseState

sealed class NotificationState : BaseState() {

    data class NotificationsFetched(val notifications: List<Notification>) : NotificationState()

}
