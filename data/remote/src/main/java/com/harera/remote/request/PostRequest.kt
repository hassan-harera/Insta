package com.harera.remote.request

import kotlinx.serialization.SerialName

data class PostRequest(
    @SerialName("caption")
    val caption: String,
    @SerialName("imageUrl")
    val imageUrl: String = "imageUrl",
)
