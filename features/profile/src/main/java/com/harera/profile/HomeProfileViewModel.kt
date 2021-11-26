package com.harera.profile

import androidx.lifecycle.viewModelScope
import com.harera.base.base.BaseViewModel
import com.harera.base.datastore.LocalStore
import com.harera.base.state.BaseState
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
) : BaseViewModel<HomeProfileState>(userDatastore) {

    var intent = Channel<HomeProfileIntent>(Channel.UNLIMITED)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            processIntent()
        }
    }

    private suspend fun processIntent() {
        intent.consumeAsFlow().collect { intent ->
            when (intent) {
                is HomeProfileIntent.GetProfile -> {
                    state = BaseState.Loading()
                    getProfile()
                }

                is HomeProfileIntent.GetPosts -> {
                    state = BaseState.Loading()
                    getPosts()
                }
            }
        }
    }

    private suspend fun getPosts() {
        postRepository
            .getProfilePosts(token!!)
            .onSuccess {
                state = HomeProfileState.PostsFetched(it)
            }
            .onFailure {
                state = BaseState.Error(it.message)
                handleFailure(it)
            }
    }

    private suspend fun getProfile() {
        profileRepository
            .getProfile(token!!)
            .onSuccess {
                state = HomeProfileState.ProfilePrepared(user = it)
            }
            .onFailure {
                state = BaseState.Error(it.message)
                handleFailure(it)
            }
    }
}


