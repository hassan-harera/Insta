package com.harera.chat

import com.harera.model.model.Message
import com.harera.model.model.Profile

sealed class ChatState {
    object Idle : ChatState()
    data class ProfileState(val profile: Profile) : ChatState()
    data class Messages(val messages: List<Message>) : ChatState()
}
