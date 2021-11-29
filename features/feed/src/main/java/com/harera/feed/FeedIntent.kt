package com.harera.feed

sealed class FeedIntent {
    object FetchPosts : FeedIntent()
    object Free : FeedIntent()
    object LoadMorePosts : FeedIntent()
}