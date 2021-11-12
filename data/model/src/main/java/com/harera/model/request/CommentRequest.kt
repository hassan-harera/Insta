package com.harera.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentRequest(
    @SerialName("pid")
    val postId: Int,
    @SerialName("content")
    val comment: String,
)