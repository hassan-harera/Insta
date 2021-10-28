package com.harera.posting

import android.net.Uri

sealed class PostingIntent {
    data class Post(val caption: String, val imageUri: Uri) : PostingIntent()
    object None : PostingIntent()
}
