package com.harera.model.model

import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    val postId: Int,
    val comment: String,
    val commentId: Int,
    var username: String,
    var time: String,
)