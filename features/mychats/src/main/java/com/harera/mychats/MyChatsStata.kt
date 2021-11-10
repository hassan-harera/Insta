package com.harera.mychats

import com.harera.base.state.State
import com.harera.model.response.Connection

sealed class MyChatsStata : State() {
    data class Connections(val connections: List<Connection>) : MyChatsStata()

}
