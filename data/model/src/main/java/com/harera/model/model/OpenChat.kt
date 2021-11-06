package com.harera.model.model

import androidx.room.PrimaryKey
import com.harera.time.InstaDateTimeSerializer
import kotlinx.serialization.Serializable
import org.joda.time.DateTime

//@Entity
data class OpenChat(
    @PrimaryKey
    var username: String,
    var profileImageUrl: String,
    var lastMessage: String,
    @Serializable(with = InstaDateTimeSerializer::class)
    var time: DateTime,
    var profileName: String,
)