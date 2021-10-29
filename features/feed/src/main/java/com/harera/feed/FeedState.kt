package com.harera.feed

import com.harera.model.model.Post


sealed class FeedState() {
    object Idle : FeedState()
    data class Loading(val message: String? = null) : FeedState()
    data class LoadingMore(val state: Boolean) : FeedState()
    data class Posts(val posts : List<Post>) : FeedState()
    data class Error(val message: String?) : FeedState()
}