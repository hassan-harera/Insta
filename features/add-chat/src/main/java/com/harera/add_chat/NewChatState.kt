package com.harera.add_chat

import com.harera.model.modelget.Profile

sealed class NewChatState {
    object Idle : NewChatState()
    data class Connections(val profiles: List<Profile>) : NewChatState()
}