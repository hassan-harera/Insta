package com.harera.model.response

import com.example.response.Notification

data class LikeNotification(
    override var type: Int,
    override var time: String,
    val likeCount: Int,
    val postId: Int,
    val postImageUrl: String,
    val profileName: String,
) : Notification()