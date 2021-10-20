package com.harera.feed


sealed class FeedIntent {
    object FetchPosts : FeedIntent()
}