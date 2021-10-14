package com.harera.model.modelset

import com.google.firebase.Timestamp

data class Post(
    var postImageUrl: String,
    var time: Timestamp,
    var caption: String,
    var postId: String,
    var uid: String,
)
