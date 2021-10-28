package com.harera.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.harera.model.modelget.Profile
import com.harera.model.modelset.Message
import com.harera.repository.db.network.abstract_.AuthManager
import com.harera.repository.db.network.abstract_.ChatRepository
import com.harera.repository.db.network.abstract_.ProfileRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import com.harera.model.modelget.Message as MessageGet

class ChatViewModel constructor(
    private val chatRepository: ChatRepository,
    private val profileRepository: ProfileRepository,
    private val authManager: AuthManager,
) : ViewModel() {

    val uid = authManager.getCurrentUser()!!.uid

    val intent = Channel<ChatIntent>(Channel.UNLIMITED)

    private val _chatState = MutableStateFlow<ChatState>(ChatState.Idle)
    val chatState: StateFlow<ChatState> = _chatState

    private val _profile = MutableStateFlow<Profile?>(null)
    val profile: StateFlow<Profile?> = _profile

    private val _messageList = MutableStateFlow<List<MessageGet>>(emptyList())
    val messageList: StateFlow<List<MessageGet>> = _messageList

    init {
        triggerIntent()
    }

    private fun triggerIntent() {
        viewModelScope.launch(Dispatchers.IO) {
            intent.consumeAsFlow().collect {
                when (it) {
                    is ChatIntent.GetMessages -> {
                        getMessages(it.messenger)
                    }

                    is ChatIntent.GetProfile -> {
                        getProfile(it.uid)
                    }
                }
            }
        }
    }

    private suspend fun getProfile(profileUid: String) {
        profileRepository.getProfile(uid = profileUid).let {
            _chatState.value = ChatState.ProfileState(profile = it)
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
                        time = Timestamp.now(),
                        from = senderUID,
                        to = receiverUID,
                        message = handledMessage
                    )
                )
        }
    }

    private fun handleMessage(message: String): String = message.replace(
        regex = "^[ \\t]+".toRegex(),
        ""
    ).replace(
        regex = "[ \\t]+\$".toRegex(),
        ""
    ).replace(
        regex = "[ \\t]+".toRegex(),
        " "
    ).replace(
        regex = "[ \\n]+\$".toRegex(),
        ""
    ).replace(
        regex = "^[ \\n]+".toRegex(),
        ""
    )


    private suspend fun getMessages(messenger: String) {
        chatRepository
            .getMessagesBetween(
                uid1 = uid,
                uid2 = messenger
            )
            .let {
                _chatState.value = ChatState.Messages(
                    it.sortedBy { it.time }
                )
            }
    }
}


