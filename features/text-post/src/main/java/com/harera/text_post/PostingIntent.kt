package com.harera.text_post

import android.graphics.Bitmap

sealed class PostingIntent {
    data class Post(val caption: String, val image: Bitmap) : PostingIntent()
    object None : PostingIntent()
}
