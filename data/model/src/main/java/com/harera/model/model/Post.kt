package com.harera.model.model

data class Post(
    val postId: Int,
    val caption: String,
    val username: String,
    var postImageUrl: String,
    val time: String,
)
