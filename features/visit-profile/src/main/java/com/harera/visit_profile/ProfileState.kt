package com.harera.visit_profile

import com.harera.model.model.Post
import com.harera.model.model.Profile


sealed class ProfileState() {
    object Idle : ProfileState()
    data class Loading(val message: String? = null) : ProfileState()
    data class PostsFetched(val postList: List<Post>) : ProfileState()
    data class ProfilePrepared(val profile: Profile) : ProfileState()
    data class Error(val message: String?) : ProfileState()
}