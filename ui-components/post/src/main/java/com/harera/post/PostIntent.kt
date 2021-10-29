package com.harera.post

sealed class PostIntent {
    data class FetchPost(val postId : String) : PostIntent()
    data class CommentToPost(val comment: String, val postId: String) : PostIntent()
    data class LikePost(val postId: String) : PostIntent()
    data class FetchPostComments(val postId: String) : PostIntent()
    data class FetchPostLikes(val postId: String) : PostIntent()
    object None : PostIntent()
}