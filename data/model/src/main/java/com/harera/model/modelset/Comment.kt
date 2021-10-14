package com.harera.model.modelset

import com.google.firebase.Timestamp

data class Comment(
    var uid: String,
    var postId: String,
    var commentId: String? = null,
    var comment: String,
    var time: Timestamp
)
