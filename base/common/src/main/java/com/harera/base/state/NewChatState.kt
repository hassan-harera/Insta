package com.harera.base.state

import com.harera.model.response.Connection

sealed class NewChatState : BaseState() {
    object Idle : NewChatState()
    data class Connections(val connections: List<Connection>) : NewChatState()
}