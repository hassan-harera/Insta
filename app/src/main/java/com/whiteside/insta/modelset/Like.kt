package com.whiteside.insta.modelset

import com.google.firebase.Timestamp
import com.whiteside.insta.modelget.Notification

class Like (
    var postId: String,
    var likeId: String? = null,
    var uid: String,
    var time: Timestamp,
) : Notification(1)