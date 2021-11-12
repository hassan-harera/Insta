package com.harera.model.response

import kotlinx.serialization.Serializable

@Serializable
data class Connection(
    val username: String,
    val profileName: String,
    val userImageUrl: String? = null,
)
