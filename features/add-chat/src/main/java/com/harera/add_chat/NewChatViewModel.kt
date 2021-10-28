package com.harera.add_chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harera.repository.db.network.abstract_.AuthManager
import com.harera.repository.db.network.abstract_.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class NewChatViewModel constructor(
    private val profileRepository: ProfileRepository,
    private val authManager: AuthManager,
) : ViewModel() {
    private val uid = authManager.getCurrentUser()!!.uid

    val newChatIntent = Channel<NewChatIntent>(capacity = Channel.UNLIMITED)

    private val _newChatState = MutableStateFlow<NewChatState>(NewChatState.Idle)
    val newChatState: StateFlow<NewChatState> = _newChatState


    init {
        triggerIntent()
    }

    private fun triggerIntent() {
        viewModelScope.launch(Dispatchers.IO) {
            newChatIntent.consumeAsFlow().collect {
                when (it) {
                    is NewChatIntent.GetConnections -> {
                        getConnections()
                    }
                }
            }
        }
    }

    private suspend fun getProfileTask(uid: String) = profileRepository.getProfile(uid = uid)


    private suspend fun getFollowings() = profileRepository.getFollowings(uid)

    private suspend fun getFollowers() = profileRepository.getFollowers(uid)


    private suspend fun getConnections() {
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

        _newChatState.value = NewChatState.Connections(
            getProfiles(connections)
        )
    }

    private suspend fun getProfiles(connections: HashSet<String>) =
        connections.map {
            profileRepository.getProfile(uid = it)
        }
}
