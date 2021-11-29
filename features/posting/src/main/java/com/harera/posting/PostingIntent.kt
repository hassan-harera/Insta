package com.harera.posting

import android.graphics.Bitmap
import android.net.Uri

sealed class PostingIntent {
    data class Post(val caption: String, val image: Bitmap) : PostingIntent()
    object None : PostingIntent()
}
