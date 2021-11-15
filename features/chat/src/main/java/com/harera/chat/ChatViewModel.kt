package com.harera.chat

import androidx.lifecycle.viewModelScope
import com.harera.base.base.BaseViewModel
import com.harera.base.datastore.LocalStore
import com.harera.base.state.ChatState
import com.harera.base.state.BaseState
import com.harera.repository.ChatRepository
import com.harera.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class ChatViewModel constructor(
    private val chatRepository: ChatRepository,
    private val profileRepository: ProfileRepository,
    userSharedPreferences: LocalStore,
) : BaseViewModel<ChatState>(userSharedPreferences) {

    val intent = Channel<ChatIntent>(Channel.UNLIMITED)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            triggerIntent()
        }
    }

    private suspend fun triggerIntent() {
        intent.consumeAsFlow().collect {
            when (it) {
                is ChatIntent.GetMessages -> {
                    getMessages(connection = it.messenger)
                }

                is ChatIntent.GetProfile -> {
                    getUser(it.username)
                }

                is ChatIntent.SendMessage -> {
                    sendMessage(it.message, it.connection)
                }
            }
        }
    }

    private suspend fun getUser(username: String) {
        profileRepository
            .getUser(token = token!!, username)
            .onSuccess {
                state = ChatState.ProfileState(it)
            }.onFailure {
                handleFailure(it)
                state = BaseState.Error(it.message)
            }
    }

    private suspend fun sendMessage(message: String, connection: String) {
        chatRepository
            .sendMessage(
                message = message,
                connection = connection,
                token = token!!,
            )
            .onSuccess {

            }
            .onFailure {
                handleFailure(it)
                state = BaseState.Error(it.message)
            }
    }


    private suspend fun getMessages(connection: String) {
        chatRepository
            .getMessages(
                token = token!!,
                connection = connection
            )
            .onSuccess {
                state = ChatState.Messages(it.sortedBy { it.time })
            }
            .onFailure {
                handleFailure(it)
                state = BaseState.Error(it.message)
            }
    }
}


