package com.harera.profile

import com.harera.base.state.BaseState
import com.harera.model.model.User
import com.harera.model.response.PostResponse

sealed class HomeProfileState : BaseState() {
    data class PostsFetched(val postList: List<PostResponse>) : HomeProfileState()
    data class ProfilePrepared(val user: User) : HomeProfileState()
}