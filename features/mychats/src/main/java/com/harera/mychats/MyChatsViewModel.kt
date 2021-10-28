package com.harera.mychats

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.harera.model.modelget.OpenChat
import com.harera.model.modelget.Profile
import com.harera.model.modelset.Message
import com.harera.repository.db.network.abstract_.AuthManager
import com.harera.repository.db.network.abstract_.ChatRepository
import com.harera.repository.db.network.abstract_.ProfileRepository
import kotlinx.coroutines.*
import com.harera.model.modelget.Message as MessageGet

class MyChatsViewModel constructor(
    private val chatRepository: ChatRepository,
    private val profileRepository: ProfileRepository,
    private val authManager: AuthManager,
) : ViewModel() {
    val profile = mutableStateOf<Profile?>(null)
    val openChats = mutableStateOf<List<OpenChat>>(emptyList())
    val messages = mutableStateOf<List<MessageGet>?>(null)
    val connectionProfiles = mutableStateOf<List<Profile>>(emptyList())
    val uid = authManager.getCurrentUser()!!.uid

    init {
        triggerIntent()
    }

    private fun triggerIntent() {

    }

    private fun getOpenChats() {
        viewModelScope.launch {
            chatRepository
                .getOpenChats(uid)
                .addOnSuccessListener {
                    viewModelScope.launch(Dispatchers.IO) {
                        it.children.map { snapshot ->
                            viewModelScope.async(Dispatchers.IO) {
                                snapshot.getValue(String::class.java)!!.let { uid ->
                                    val openChat = OpenChat()
                                    openChat.uid = uid

                                    getProfileTask(uid).apply {
                                        openChat.profileImageUrl = profileImageUrl
                                        openChat.profileName = name
                                    }

                                    getLastMessage(uid, this@MyChatsViewModel.uid).apply {
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
        profileRepository.getProfile(uid = uid)


    private suspend fun getFollowings() =
        profileRepository.getFollowings(uid)

    private suspend fun getFollowers() = profileRepository.getFollowers(uid)


    private fun getAllConnections() {
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
                profileRepository.getProfile(uid = it)
            }.let {
                connectionProfiles.value = it
            }
        }
    }

    private suspend fun getProfile(uid: String) =
        profileRepository.getProfile(uid = uid)

    private fun sendMessage(message: String, senderUID: String, receiverUID: String) {
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


    private fun getMessages(receiverUID: String, senderUID: String) {
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
