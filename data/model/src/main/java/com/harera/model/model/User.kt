package com.harera.model.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class User(
    @PrimaryKey
    val uid: Int,
    val name: String,
    val username: String,
    val password: String,
    val userImageUrl: String? = null,
    val bio: String? = null,
    val email: String,
    val phoneNumber: String? = null,
)
