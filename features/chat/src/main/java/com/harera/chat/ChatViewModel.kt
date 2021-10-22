package com.harera.chat

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Timestamp
import com.harera.model.modelget.FollowRelation
import com.harera.model.modelget.OpenChat
import com.harera.model.modelget.Profile
import com.harera.model.modelset.Message
import com.harera.repository.db.network.abstract_.AuthManager
import com.harera.repository.db.network.abstract_.ChatRepository
import com.harera.repository.db.network.abstract_.ProfileRepository
import kotlinx.coroutines.*
import com.harera.model.modelget.Message as MessageGet

class ChatViewModel constructor(
    private val chatRepository: ChatRepository,
    private val profileRepository: ProfileRepository,
    private val authManager: AuthManager,
) : ViewModel() {
    val profile = mutableStateOf<Profile?>(null)
    val openChats = mutableStateOf<List<OpenChat>>(emptyList())
    val messages = mutableStateOf<List<MessageGet>?>(null)
    val connectionProfiles = mutableStateOf<List<Profile>>(emptyList())
    val uid = authManager.getCurrentUser()!!.uid

    fun getOpenChats() {
        chatRepository
            .getOpenChats(uid)
            .addOnSuccessListener {
                viewModelScope.launch(Dispatchers.IO) {
                    it.children.map { snapshot ->
                        viewModelScope.async(Dispatchers.IO) {
                            snapshot.getValue(String::class.java)!!.let { uid ->
                                val openChat = OpenChat()
                                openChat.uid = uid

                                getProfileTask(uid).toObject(Profile::class.java)!!.apply {
                                    openChat.profileImageUrl = profileImageUrl
                                    openChat.profileName = name
                                }

                                getLastMessage(uid, this@ChatViewModel.uid).apply {
                                    openChat.lastMessage = message
                                    openChat.time = time
                                }
                                return@async openChat
                            }
                        }.await()
                    }.let {
                        openChats.value = it
                    }
                }
            }
    }

    private suspend fun getLastMessage(uid1: String, uid2: String) =
        viewModelScope.async(Dispatchers.IO) {
            chatRepository.getLastMessage(uid1 = uid1, uid2 = uid2).map {
                it.toObject(com.harera.model.modelget.Message::class.java)!!
            }.sortedByDescending {
                it.time
            }[0]
        }.await()

    private suspend fun getProfileTask(uid: String) =
        viewModelScope.async(Dispatchers.IO) {
            Tasks.await(
                profileRepository.getProfile(uid = uid)
            )
        }.await()


    private suspend fun getFollowings() =
        viewModelScope.async(Dispatchers.IO) {
            Tasks.await(profileRepository.getFollowings(uid))
                .documents.map {
                    it.toObject(FollowRelation::class.java)!!
                }.let {
                    return@async it
                }
        }.await()

    private suspend fun getFollowers() =
        viewModelScope.async(Dispatchers.IO) {
            Tasks.await(profileRepository.getFollowers(uid))
                .documents.map {
                    it.toObject(FollowRelation::class.java)!!
                }.let {
                    return@async it
                }
        }.await()

    fun getAllConnections() {
        viewModelScope.launch(Dispatchers.IO) {
            val connections = HashSet<String>()

            getFollowers().map {
                it.followerUid
            }.let {
                connections.addAll(it)
            }

            getFollowings().map {
                it.followingUid
            }.let {
                connections.addAll(it)
            }

            getProfiles(connections)
        }
    }

    private fun getProfiles(connections: HashSet<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            connections.map {
                async(Dispatchers.IO) {
                    Tasks.await(profileRepository.getProfile(uid = it))
                        .toObject(Profile::class.java)!!
                }.await()
            }.let {
                connectionProfiles.value = it
            }
        }
    }

    fun getProfile(uid: String) {
        profileRepository.getProfile(uid = uid)
            .addOnSuccessListener {
                profile.value = it.toObject(Profile::class.java)!!
            }
    }

    fun sendMessage(message: String, senderUID: String, receiverUID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val handledMessage = runBlocking {
                return@runBlocking handleMessage(message)
            }

            chatRepository
                .saveMessage(
                    Message(
                        time = Timestamp.now(),
                        from = senderUID,
                        to = receiverUID,
                        message = handledMessage
                    )
                )
        }
    }

    private fun handleMessage(message: String): String = message.replace(
        regex = "^[ \\t]+".toRegex(),
        ""
    ).replace(
        regex = "[ \\t]+\$".toRegex(),
        ""
    ).replace(
        regex = "[ \\t]+".toRegex(),
        " "
    ).replace(
        regex = "[ \\n]+\$".toRegex(),
        ""
    ).replace(
        regex = "^[ \\n]+".toRegex(),
        ""
    )


    fun getMessages(receiverUID: String, senderUID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            //TODO handle delay
            delay(1000)
            chatRepository
                .getMessages(
                    senderUID = senderUID,
                    receiverUID = receiverUID
                ).map {
                    it.toObject(MessageGet::class.java)!!
                }
                .let {
                    messages.value = it.sortedBy {
                        it.time
                    }
                }
        }
    }
}
