package com.harera.repository.db.network.firebase

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.harera.model.modelset.Chat
import com.harera.model.modelset.Message
import com.harera.repository.common.Constansts.CHATS
import com.harera.repository.common.Constansts.MESSAGES
import com.harera.repository.common.Constansts.USERS
import com.harera.repository.db.network.abstract_.AuthManager
import com.harera.repository.db.network.abstract_.ChatRepository
import javax.inject.Inject


class FirebaseChatRepository @Inject constructor(
    val fStore: FirebaseFirestore,
    private val dbReferences: FirebaseDatabase,
    val authManager: AuthManager
) : ChatRepository {

    override fun getMessage(messageId: String): Task<Void> {
        TODO("Not yet implemented")
    }

    override fun getMessages(receiverUID: String, senderUID: String) =
        Tasks.await(
            fStore
                .collection(MESSAGES)
                .whereEqualTo(Message::from.name, senderUID)
                .whereEqualTo(Message::to.name, receiverUID)
                .get()
        ).documents
            .plus(
                Tasks.await(
                    fStore
                        .collection(MESSAGES)
                        .whereEqualTo(Message::from.name, receiverUID)
                        .whereEqualTo(Message::to.name, senderUID)
                        .get()
                ).documents
            )

    override fun saveMessage(message: Message) =
        fStore
            .collection(MESSAGES)
            .document()
            .set(message)

    override fun getOpenChats(uid: String): Task<DataSnapshot> =
        dbReferences.reference
            .child(USERS)
            .child(uid)
            .child(CHATS)
            .get()

    override fun addOpenChat(chat: Chat): Task<Void> =
        dbReferences.reference
            .child(USERS)
            .child(chat.firstUid)
            .child(CHATS)
            .child(chat.secondUid)
            .setValue(chat.secondUid)
            .continueWithTask {
                dbReferences.reference
                    .child(USERS)
                    .child(chat.secondUid)
                    .child(CHATS)
                    .child(chat.firstUid)
                    .setValue(chat.firstUid)
            }

    override fun getLastMessage(uid2: String, uid1: String) =
        Tasks.await(
            fStore
                .collection(MESSAGES)
                .whereEqualTo(Message::from.name, uid2)
                .whereEqualTo(Message::to.name, uid1)
                .get()
        ).documents
            .plus(
                Tasks.await(
                    fStore
                        .collection(MESSAGES)
                        .whereEqualTo(Message::from.name, uid1)
                        .whereEqualTo(Message::to.name, uid2)
                        .get()
                )
            )
}
