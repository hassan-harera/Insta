package com.whiteside.insta.model

import com.google.firebase.Timestamp

open class Notification(val type: Int)

class LikeNotification(
    var postID: String,
    var UID: String,
    var date: Timestamp,
) : Notification(1)

class FriendRequestNotification(
    var UID: String? = null,
    var date: Timestamp,
) : Notification(2)