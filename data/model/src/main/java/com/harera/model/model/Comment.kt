package com.harera.model.model


data class Comment(
    val postId: Int,
    val comment: String,
    var username: String,
    var time: String,
)