package com.harera.visit_profile

import com.harera.base.state.BaseState
import com.harera.model.model.User
import com.harera.model.response.PostResponse

sealed class VisitProfileState : BaseState() {
    object Idle : VisitProfileState()
    data class Loading(val message: String? = null) : VisitProfileState()
    data class PostsFetched(val postList: List<PostResponse>) : VisitProfileState()
    data class ProfilePrepared(val user: User) : VisitProfileState()
    data class Error(val message: String?) : VisitProfileState()
}