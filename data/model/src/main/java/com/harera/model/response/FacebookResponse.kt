package com.harera.model.response

import kotlinx.serialization.Serializable

@Serializable
data class FacebookResponse(
    val name: String,
    val id: String,
)
