package com.harera.repository

import com.harera.model.model.Message
import com.harera.model.response.ChatResponse
import com.harera.model.response.MessageResponse

interface ChatRepository {
    suspend fun getChats(token: String): Result<List<ChatResponse>>
    suspend fun getMessages(token: String, connection: String): Result<List<MessageResponse>>
    suspend fun getMessage(messageId: String): Result<Message>
    suspend fun sendMessage(token: String, message: String, connection: String): Result<String>
}