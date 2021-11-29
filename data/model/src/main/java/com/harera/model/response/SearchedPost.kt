package com.harera.model.response

import kotlinx.serialization.Serializable

@Serializable
data class SearchedPost(
    val postId: Int,
    val caption: String,
    val username: String,
    val profileName: String,
    val userImageUrl: String? = null,
    var postImageUrl: String,
    val time: String,
)
