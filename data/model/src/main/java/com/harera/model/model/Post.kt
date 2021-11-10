package com.harera.model.model

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val postId: Int,
    val caption: String,
    val username: String,
    var postImageUrl: String,
    val time: String,
)
