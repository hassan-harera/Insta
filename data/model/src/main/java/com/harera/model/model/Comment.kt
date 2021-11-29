package com.harera.model.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class Comment(
    @PrimaryKey
    val postId: Int,
    val comment: String,
    val commentId: Int,
    var username: String,
    var time: String,
    val userImageUrl: String? = null,
    val profileName: String,
)