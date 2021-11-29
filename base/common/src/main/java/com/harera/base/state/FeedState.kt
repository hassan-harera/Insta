package com.harera.base.state

import com.harera.model.response.PostResponse


sealed class FeedState : BaseState() {
    object Idle : FeedState()
    data class LoadingMore(val state: Boolean) : FeedState()
    data class Posts(val posts: List<PostResponse>) : FeedState()
    data class MorePosts(val posts: List<PostResponse>) : FeedState()
}