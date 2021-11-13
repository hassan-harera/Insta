package com.harera.model.response

import androidx.room.Entity
import com.harera.model.model.Comment
import com.harera.model.model.Like
import com.harera.model.model.Post
import com.harera.model.model.User
import kotlinx.serialization.Serializable

@Serializable
data class PostResponse(
    var post: Post,
    var comments: List<Comment> = emptyList(),
    var likes: List<Like> = emptyList(),
    val user: User,
)