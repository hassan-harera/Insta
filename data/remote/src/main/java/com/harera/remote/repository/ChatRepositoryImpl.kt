package com.harera.remote.repository

import com.harera.model.model.Message
import com.harera.model.response.ChatResponse
import com.harera.model.response.MessageResponse
import com.harera.remote.request.MessageInsertRequest
import com.harera.remote.service.MessageService
import com.harera.repository.ChatRepository

class ChatRepositoryImpl(
    private val messageService: MessageService,
) : ChatRepository {


    override suspend fun getMessages(
        token: String,
        connection: String,
    ): Result<List<MessageResponse>> = kotlin.runCatching {
        messageService
            .getMessages(token = token, connection)
    }

    override suspend fun getMessage(messageId: String): Result<Message> {
        TODO("Not yet implemented")
    }

    override suspend fun sendMessage(
        token: String,
        message: String,
        connection: String,
    ): Result<String> = kotlin.runCatching {
        messageService
            .insertMessage(
                token = token,
                request = MessageInsertRequest(
                    message = message,
                    receiver = connection
                )
            )
    }

    override suspend fun getChats(token: String): Result<List<ChatResponse>> = kotlin.runCatching {
        messageService
            .getChats(token = token)
    }
}
