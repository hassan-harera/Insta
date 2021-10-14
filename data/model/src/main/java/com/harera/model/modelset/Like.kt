package com.harera.model.modelset

import com.google.firebase.Timestamp
import com.harera.model.modelget.Notification

class Like (
    var postId: String,
    var likeId: String? = null,
    var uid: String,
    var time: Timestamp,
) : Notification(1)