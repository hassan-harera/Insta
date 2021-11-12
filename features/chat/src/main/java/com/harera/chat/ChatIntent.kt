package com.harera.chat

sealed class ChatIntent {
    data class GetProfile(val username: String) : ChatIntent()
    data class GetMessages(val messenger: String) : ChatIntent()
    data class SendMessage(val message: String, val connection: String) : ChatIntent()
}
