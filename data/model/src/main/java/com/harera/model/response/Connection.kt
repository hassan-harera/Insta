package com.harera.model.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class Connection(
    @PrimaryKey
    val username: String,
    val profileName: String,
    val userImageUrl: String? = null,
)
