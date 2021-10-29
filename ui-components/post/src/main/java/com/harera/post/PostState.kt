package com.harera.post

import com.harera.model.model.Comment
import com.harera.model.model.Like
import com.harera.model.model.Post


sealed class PostState() {
    object Idle : PostState()
    data class Loading(val message: String? = null) : PostState()
    data class Error(val message: String?) : PostState()
    data class PostFetched(val post: Post) : PostState()
    data class CommentsFetched(val comments: List<Comment>) : PostState()
    data class LikesFetched(val likes: List<Like>) : PostState()
    data class CommentAdded(val comment: Comment) : PostState()
    data class LikeAdded(val like: Like) : PostState()
}