package com.harera.base.state

import com.harera.model.model.User
import com.harera.model.response.MessageResponse

sealed class ChatState : State() {
    object Idle : ChatState()
    data class ProfileState(val user: User) : ChatState()
    data class Messages(val messages: List<MessageResponse>) : ChatState()
}
