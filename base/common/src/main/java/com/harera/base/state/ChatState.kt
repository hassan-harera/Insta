package com.harera.base.state

import com.harera.model.model.Message
import com.harera.model.model.User

sealed class ChatState : State() {
    object Idle : ChatState()
    data class ProfileState(val user: User) : ChatState()
    data class Messages(val messages: List<Message>) : ChatState()
}
