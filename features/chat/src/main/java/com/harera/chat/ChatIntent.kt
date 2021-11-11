package com.harera.chat

sealed class ChatIntent {
    object Free : ChatIntent()
    data class GetProfile(val uid: String) : ChatIntent()
    data class StartListen(val messenger: String) : ChatIntent()
    data class SendMessage(val message: String, val connection: String) : ChatIntent()
}
