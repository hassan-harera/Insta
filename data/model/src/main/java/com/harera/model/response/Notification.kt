package com.harera.model.response

import kotlinx.serialization.Serializable

@Serializable
data class Notification(
    var type: Int,
    var time: String,
    var likeCount: Int? = null,
    var commentCount: Int? = null,
    var postId: Int,
    var postImageUrl: String,
    var profileName: String,
)