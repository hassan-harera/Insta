package com.harera.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.firestore.DocumentSnapshot
import com.harera.model.model.Chat
import com.harera.model.model.Message

interface ChatRepository {

    suspend fun getMessage(messageId: String): Task<Void>
    suspend fun saveMessage(message: Message): Task<Void>
    suspend fun getChats(token: String): Task<DataSnapshot>
    suspend fun addOpenChat(chat: Chat): Task<Void>
    suspend fun getLastMessage(uid2: String, uid1: String): List<DocumentSnapshot>
    suspend fun getMessages(token: String, connection: String): Result<List<Message>>
}