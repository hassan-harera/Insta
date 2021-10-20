package com.harera.feed

import com.harera.model.modelget.Post


sealed class FeedState() {
    object Idle : FeedState()
    data class Loading(val message: String? = null) : FeedState()
    data class Posts(val postList: List<Post>) : FeedState()
    data class Error(val message: String?) : FeedState()
}