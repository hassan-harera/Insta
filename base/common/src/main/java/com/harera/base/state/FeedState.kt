package com.harera.base.state

import com.harera.model.response.PostResponse


sealed class FeedState : State() {
    object Idle : FeedState()
    data class Loading(val message: String? = null) : FeedState()
    data class LoadingMore(val state: Boolean) : FeedState()
    data class Posts(val posts: List<PostResponse>) : FeedState()
    data class Error(val message: String?) : FeedState()
}