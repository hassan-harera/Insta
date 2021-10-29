package com.harera.repository.db.network.abstract_

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.firestore.DocumentSnapshot
import com.harera.model.model.Chat
import com.harera.model.model.Message
import com.harera.model.model.Message as MessageGet

interface ChatRepository {

    suspend fun getMessage(messageId: String): Task<Void>
    suspend fun getMessages(receiverUID: String, senderUID: String): List<DocumentSnapshot>
    suspend fun saveMessage(message: Message): Task<Void>
    suspend fun getOpenChats(uid: String): Task<DataSnapshot>
    suspend fun addOpenChat(chat: Chat): Task<Void>
    suspend fun getLastMessage(uid2: String, uid1: String): List<DocumentSnapshot>
    suspend fun getMessagesBetween(uid1: String, uid2: String): List<MessageGet>
}
