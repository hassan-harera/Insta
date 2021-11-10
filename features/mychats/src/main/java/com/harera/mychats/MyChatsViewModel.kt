package com.harera.mychats

import androidx.lifecycle.viewModelScope
import com.harera.base.base.BaseViewModel
import com.harera.base.datastore.LocalStore
import com.harera.base.state.PostState
import com.harera.repository.ChatRepository
import com.harera.repository.ProfileRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow

class MyChatsViewModel constructor(
    private val chatRepository: ChatRepository,
    private val profileRepository: ProfileRepository,
    userSharedPreferences: LocalStore,
) : BaseViewModel<MyChatsStata>(userSharedPreferences) {

//    val profile = mutableStateOf<User?>(null)
//    val openChats = mutableStateOf<List<OpenChat>>(emptyList())

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
                state = MyChatsStata.Connections(it)
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
