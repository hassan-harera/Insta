package com.harera.text_post

import android.net.Uri

sealed class PostingIntent {
    data class Post(val caption: String, val imageUri: Uri) : PostingIntent()
    object None : PostingIntent()
}
