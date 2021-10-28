package com.harera.add_chat

sealed class NewChatIntent {
    object GetConnections : NewChatIntent()
    object Free : NewChatIntent()
}
