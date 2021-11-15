package com.harera.mychats

import com.harera.base.state.BaseState
import com.harera.model.response.ChatResponse

sealed class MyChatsState : BaseState() {
    data class Chats(val connections: List<ChatResponse>) : MyChatsState()

}
