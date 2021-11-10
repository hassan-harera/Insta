package com.harera.chat

import androidx.lifecycle.viewModelScope
import com.harera.base.base.BaseViewModel
import com.harera.base.datastore.LocalStore
import com.harera.base.state.ChatState
import com.harera.base.state.PostState
import com.harera.base.state.State
import com.harera.base.utils.message.MessageUtils.handleMessage
import com.harera.model.model.Message
import com.harera.repository.ChatRepository
import com.harera.repository.ProfileRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import org.joda.time.DateTime
import java.util.*

class ChatViewModel constructor(
    private val chatRepository: ChatRepository,
    private val profileRepository: ProfileRepository,
    userSharedPreferences: LocalStore,
) : BaseViewModel<ChatState>(userSharedPreferences) {

    val intent = Channel<ChatIntent>(Channel.UNLIMITED)

    init {
        triggerIntent()
    }

    private fun triggerIntent() {
        viewModelScope.launch(Dispatchers.IO) {
            intent.consumeAsFlow().collect {
                when (it) {
                    is ChatIntent.GetMessages -> {
                        while (true) {
                            delay(1000)
                            getMessages(it.messenger)
                        }
                    }

                    is ChatIntent.GetProfile -> {
                        getUser(it.uid)
                    }
                }
            }


        }
    }

    private suspend fun getUser(username: String) {
        profileRepository
            .getUser(token = token!!, username)
            .onSuccess {

            }.onFailure {
                this.state = PostState.Error(it.message)
                handleFailure(it)
            }
    }

    private fun sendMessage(message: String, senderUID: String, receiverUID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val handledMessage = runBlocking {
                return@runBlocking handleMessage(message)
            }

            chatRepository
                .saveMessage(
                    Message(
                        time = DateTime.now(),
                        sender = senderUID,
                        receiver = receiverUID,
                        message = handledMessage,
                    )
                )
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
                state = State.Error(it.message)
            }
    }
}


