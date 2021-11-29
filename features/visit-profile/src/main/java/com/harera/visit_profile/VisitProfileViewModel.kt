package com.harera.visit_profile

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

class VisitProfileViewModel constructor(
    private val profileRepository: ProfileRepository,
    private val postRepository: PostRepository,
    userSharedPreferences: LocalStore,
) : BaseViewModel<VisitProfileState>(userSharedPreferences) {

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
                state = VisitProfileState.PostsFetched(it)
            }
            .onFailure {
                state = BaseState.Error(it.message)
                handleFailure(it)
            }
    }

    private suspend fun getProfile() {
        state = VisitProfileState.Loading()

        profileRepository
            .getProfile(uid)
            .onSuccess {
                state = VisitProfileState.ProfilePrepared(it)
            }
            .onFailure {
                handleFailure(it)
            }
    }
}


