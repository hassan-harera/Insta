package com.harera.profile

import androidx.lifecycle.viewModelScope
import com.harera.base.base.BaseViewModel
import com.harera.base.datastore.LocalStore
import com.harera.base.state.ProfileState
import com.harera.base.state.State
import com.harera.repository.PostRepository
import com.harera.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class HomeProfileViewModel constructor(
    private val profileRepository: ProfileRepository,
    private val postRepository: PostRepository,
    userDatastore: LocalStore,
) : BaseViewModel<ProfileState>(userDatastore) {

    var intent = Channel<ProfileIntent>(Channel.UNLIMITED)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            processIntent()
        }
    }

    private suspend fun processIntent() {
        intent.consumeAsFlow().collect { intent ->
            when (intent) {
                is ProfileIntent.GetProfile -> {
                    getProfile()
                }

                is ProfileIntent.GetPosts -> {
                    getPosts()
                }
            }
        }
    }

    private suspend fun getPosts() {
        state = ProfileState.Loading()

        postRepository
            .getProfilePosts(token!!)
            .onSuccess {
                state = ProfileState.PostsFetched(it)
            }
            .onFailure {
                state = State.Error(it.message)
                handleFailure(it)
            }
    }

    private suspend fun getProfile() {
        state = ProfileState.Loading()

        profileRepository
            .getProfile(token!!)
            .onSuccess {
                state = ProfileState.ProfilePrepared(it)
            }
            .onFailure {
                state = State.Error(it.message)
                handleFailure(it)
            }
    }
}


