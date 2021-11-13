package com.harera.mychats

import androidx.lifecycle.viewModelScope
import com.harera.base.base.BaseViewModel
import com.harera.base.datastore.LocalStore
import com.harera.base.state.PostState
import com.harera.repository.ChatRepository
import com.harera.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class MyChatsViewModel constructor(
    private val chatRepository: ChatRepository,
    private val profileRepository: ProfileRepository,
    userSharedPreferences: LocalStore,
) : BaseViewModel<MyChatsState>(userSharedPreferences) {

    val intent = Channel<ChatIntent>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            triggerIntent()
        }
    }

    private suspend fun triggerIntent() {
        intent.consumeAsFlow().collect {
            when (it) {
                is ChatIntent.GetChats -> {
                    getChats()
                }
            }
        }
    }

    private suspend fun getChats() {
        chatRepository
            .getChats(token!!)
            .onSuccess {
                state = MyChatsState.Chats(it)
            }
            .onFailure {
                state = PostState.Error(it.message)
                handleFailure(it)
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
}
