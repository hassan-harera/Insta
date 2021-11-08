package com.harera.model.request

import com.google.gson.annotations.SerializedName
import java.io.File

data class PostRequest(
    @SerializedName("caption")
    val caption: String,
    @SerializedName("imageUrl")
    val postImageUrl: File,
)
