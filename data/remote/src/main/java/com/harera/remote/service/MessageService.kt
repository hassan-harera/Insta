package com.harera.remote.service

import com.harera.model.response.ChatResponse
import com.harera.model.response.MessageResponse
import com.harera.remote.Routing
import com.harera.remote.URL
import com.harera.remote.request.MessageInsertRequest
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

interface MessageService {
    suspend fun insertMessage(token: String, request: MessageInsertRequest): String
    suspend fun deleteMessage(token: String, messageId: Int): String
    suspend fun getMessage(token: String, messageId: Int): MessageResponse
    suspend fun getMessages(token: String, username: String): List<MessageResponse>
    suspend fun getChats(token: String): List<ChatResponse>
}

class MessageServiceImpl(private val client: HttpClient) : MessageService {

    override suspend fun insertMessage(token: String, request: MessageInsertRequest) =
        client.post<String> {
            url(Routing.INSERT_MESSAGE)
            contentType(ContentType.Application.Json)
            body = request
        }

    override suspend fun deleteMessage(token: String, messageId: Int) =
        client.delete<String> {
            url(URL.BASE_URL.plus("/message/$messageId"))
        }

    override suspend fun getMessage(token: String, messageId: Int): MessageResponse =
        client.get<MessageResponse> {
            url(URL.BASE_URL.plus("/message/$messageId"))
        }

    override suspend fun getMessages(token: String, username: String): List<MessageResponse> =
        client.get<List<MessageResponse>> {
            url(URL.BASE_URL.plus("/chat/$username"))
        }

    override suspend fun getChats(token: String): List<ChatResponse> =
        client.get<List<ChatResponse>> {
            url(URL.BASE_URL.plus("/chats"))
        }
}