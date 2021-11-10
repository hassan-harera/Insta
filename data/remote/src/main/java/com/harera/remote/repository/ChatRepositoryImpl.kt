package com.harera.remote.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.firestore.DocumentSnapshot
import com.harera.model.model.Chat
import com.harera.model.model.Message
import com.harera.repository.ChatRepository

class ChatRepositoryImpl : ChatRepository {
    override suspend fun getMessage(messageId: String): Task<Void> {
        TODO("Not yet implemented")
    }

    override suspend fun saveMessage(message: Message): Task<Void> {
        TODO("Not yet implemented")
    }

    override suspend fun getChats(token: String): Result<Chat> {
        TODO("Not yet implemented")
    }

    override suspend fun addOpenChat(chat: Chat): Task<Void> {
        TODO("Not yet implemented")
    }

    override suspend fun getLastMessage(uid2: String, uid1: String): List<DocumentSnapshot> {
        TODO("Not yet implemented")
    }

    override suspend fun getMessages(token: String, connection: String): Result<List<Message>> {
        TODO("Not yet implemented")
    }
}
