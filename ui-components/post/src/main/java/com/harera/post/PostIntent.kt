package com.harera.post

sealed class PostIntent {
    data class FetchPost(val postId: Int) : PostIntent()
    data class CommentToPost(val comment: String, val postId: Int) : PostIntent()
    data class LikePost(val postId: Int) : PostIntent()
    data class FetchPostComments(val postId: Int) : PostIntent()
    data class FetchPostLikes(val postId: Int) : PostIntent()
    object None : PostIntent()
}