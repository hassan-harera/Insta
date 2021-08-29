package com.whiteside.insta.db.network.abstract_

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.whiteside.insta.modelset.Message
import com.whiteside.insta.modelset.OpenChat

interface ChatRepository {

    fun getMessage(messageId: String): Task<Void>
    fun getMessages(uid: String): Task<DataSnapshot>
    fun saveMessage(message: Message): Task<Void>
    fun getOpenChats(uid: String): Task<DataSnapshot>
    fun addChat(uid: String, chat: OpenChat): Task<Void>
    fun getChat(uid: String, messenger: String): Task<DataSnapshot>
}
