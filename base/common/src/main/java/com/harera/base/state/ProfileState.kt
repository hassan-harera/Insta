package com.harera.base.state

import com.harera.model.model.User
import com.harera.model.response.PostResponse

sealed class ProfileState : State() {
    object Idle : ProfileState()
    data class Loading(val message: String? = null) : ProfileState()
    data class PostsFetched(val postList: List<PostResponse>) : ProfileState()
    data class ProfilePrepared(val user: User) : ProfileState()
    data class Error(val message: String?) : ProfileState()
}