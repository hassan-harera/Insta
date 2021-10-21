package com.harera.profile

import com.harera.model.modelget.Post
import com.harera.model.modelget.Profile


sealed class ProfileState() {
    object Idle : ProfileState()
    data class Loading(val message: String? = null) : ProfileState()
    data class Posts(val postList: List<Post>) : ProfileState()
    data class ProfilePrepared(val profile: Profile) : ProfileState()
    data class Error(val message: String?) : ProfileState()
}