package com.harera.feed


sealed class FeedState() {
    object Idle : FeedState()
    data class Loading(val message: String? = null) : FeedState()
    data class LoadingMore(val state: Boolean) : FeedState()
    object Posts : FeedState()
    data class Error(val message: String?) : FeedState()
}