package com.harera.mychats

import com.harera.model.response.ChatResponse
import com.harera.base.state.State

sealed class MyChatsState : State() {
    data class Chats(val connections: List<ChatResponse>) : MyChatsState()

}
