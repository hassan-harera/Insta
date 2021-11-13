package com.harera.mychats

import com.harera.base.state.State
import com.harera.model.response.ChatResponse

sealed class MyChatsState : State() {
    data class Chats(val connections: List<ChatResponse>) : MyChatsState()

}
