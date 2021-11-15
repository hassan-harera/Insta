package com.harera.base.state

import com.harera.model.model.Comment
import com.harera.model.model.Like
import com.harera.model.model.User
import com.harera.model.response.PostResponse


sealed class PostState : BaseState() {
    object Idle : PostState()
    data class Loading(val message: String? = null) : PostState()
    data class Error(val message: String?) : PostState()
    data class PostFetched(val post: PostResponse) : PostState()
    data class CommentsFetched(val comments: List<Comment>) : PostState()
    data class LikesFetched(val likes: List<Like>) : PostState()
}