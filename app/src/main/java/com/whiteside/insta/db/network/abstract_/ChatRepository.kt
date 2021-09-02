package com.whiteside.insta.db.network.abstract_

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.firestore.DocumentSnapshot
import com.whiteside.insta.modelset.Chat
import com.whiteside.insta.modelset.Message

interface ChatRepository {

    fun getMessage(messageId: String): Task<Void>
    fun getMessages(receiverUID: String, senderUID: String): List<DocumentSnapshot>
    fun saveMessage(message: Message): Task<Void>
    fun getOpenChats(uid: String): Task<DataSnapshot>
    fun addOpenChat(chat: Chat): Task<Void>
    fun getLastMessage(uid2: String, uid1: String): List<DocumentSnapshot>
}
