package com.harera.visit_profile

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

class VisitProfileViewModel constructor(
    private val profileRepository: ProfileRepository,
    private val postRepository: PostRepository,
    userSharedPreferences: LocalStore,
) : BaseViewModel<ProfileState>(userSharedPreferences) {

    private lateinit var uid: String
    fun setUid(uid: String) {
        this.uid = uid
    }

    var intent = Channel<ProfileIntent>()

    init {
        processIntent()
    }

    private fun processIntent() {
        viewModelScope.launch(Dispatchers.IO) {
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
    }

    private suspend fun getPosts() {
        postRepository
            .getUserPosts(username = uid, token!!)
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
            .getProfile(uid)
            .onSuccess {
                state = ProfileState.ProfilePrepared(it)
            }
            .onFailure {
                handleFailure(it)
            }
    }
}


