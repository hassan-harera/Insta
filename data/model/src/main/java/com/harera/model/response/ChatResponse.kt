package com.harera.model.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class ChatResponse(
    @PrimaryKey
    val username: String,
    var name: String,
    var userImageUrl: String? = null,
    var lastMessage: String,
    var time: String,
)