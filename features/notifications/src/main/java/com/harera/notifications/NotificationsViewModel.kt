package com.harera.notifications

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.harera.base.base.BaseViewModel
import com.harera.base.datastore.LocalStore
import com.harera.repository.NotificationsRepository
import com.harera.repository.ProfileRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class NotificationsViewModel(
    private val notificationsRepository: NotificationsRepository,
    userSharedPreferences: LocalStore,
) : BaseViewModel<NotificationState>(userSharedPreferences) {

    val intent = Channel<NotificationIntent>(Channel.UNLIMITED)
    private var page by mutableStateOf(0)
    private var pageSize by mutableStateOf(25)

    init {
        viewModelScope.launch {
            triggerIntent()
        }
    }

    private suspend fun triggerIntent() {
        intent.consumeAsFlow().collect {
            when (it) {
                is NotificationIntent.GetNotifications -> {
                    getNotifications()
                    page++
                }
            }
        }
    }

    private suspend fun getNotifications() {
        notificationsRepository
            .getNotifications(token = token!!, pageSize = pageSize, page = page)
            .onSuccess {
                state = NotificationState.NotificationsFetched(it)
            }
            .onFailure {
                handleFailure(it)
            }
    }
}