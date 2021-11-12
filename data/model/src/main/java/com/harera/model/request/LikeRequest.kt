package com.harera.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LikeRequest(
    @SerialName("pid")
    val postId: Int,
)
