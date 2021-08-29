package com.whiteside.dwaa.network.repository.firebase

import android.os.Messenger
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.whiteside.insta.common.Constansts.CHATS
import com.whiteside.insta.common.Constansts.USERS
import com.whiteside.insta.db.network.abstract_.AuthManager
import com.whiteside.insta.db.network.abstract_.ChatRepository
import com.whiteside.insta.modelset.Message
import com.whiteside.insta.modelset.OpenChat
import javax.inject.Inject


class FirebaseChatRepository @Inject constructor(
    val firestore: FirebaseFirestore,
    val dbReferences: FirebaseDatabase,
    val authManager: AuthManager
) : ChatRepository {

    override fun getMessage(messageId: String): Task<Void> {
        TODO("Not yet implemented")
    }

    override fun getMessages(uid: String): Task<DataSnapshot> =
        dbReferences
            .reference
            .child(USERS)
            .child(authManager.getCurrentUser()!!.uid)
            .child(CHATS)
            .child(uid)
            .get()

    override fun saveMessage(message: Message) =
        dbReferences.reference
            .child(USERS)
            .child(message.from)
            .child(CHATS)
            .child(message.to)
            .push()
            .setValue(message)

    override fun getOpenChats(uid: String): Task<DataSnapshot> =
        dbReferences.reference
            .child(USERS)
            .child(uid)
            .child(CHATS)
            .get()

    override fun addChat(uid: String, chat : OpenChat): Task<Void> =
        dbReferences.reference
            .child(USERS)
            .child(uid)
            .child(CHATS)
            .child(chat.receiver)
            .setValue(chat)

    override fun getChat(uid: String, messenger: String): Task<DataSnapshot> =
        dbReferences.reference
            .child(USERS)
            .child(uid)
            .child(CHATS)
            .child(messenger)
            .get()
}