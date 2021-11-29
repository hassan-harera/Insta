package com.harera.model.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class Post(
    @PrimaryKey
    val postId: Int,
    val caption: String,
    val username: String,
    val type: Int,
    var postImageUrl: String,
    val time: String,
)
