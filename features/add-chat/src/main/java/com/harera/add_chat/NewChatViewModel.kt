package com.harera.add_chat

import androidx.lifecycle.viewModelScope
import com.harera.base.base.BaseViewModel
import com.harera.base.datastore.LocalStore
import com.harera.base.state.NewChatState
import com.harera.base.state.PostState
import com.harera.base.state.State
import com.harera.model.model.User
import com.harera.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class NewChatViewModel constructor(
    private val profileRepository: ProfileRepository,
    userSharedPreferences: LocalStore,
) : BaseViewModel<NewChatState>(userSharedPreferences) {

    val newChatIntent = Channel<NewChatIntent>(capacity = Channel.UNLIMITED)

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

    private suspend fun getConnections() {
        profileRepository
            .getConnections(token!!)
            .onSuccess {
                state = NewChatState.Connections(it)
            }
            .onFailure {
                state = State.Error(it)
                handleFailure(it)
            }
    }
}
