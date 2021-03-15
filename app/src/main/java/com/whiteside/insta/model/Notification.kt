package com.whiteside.insta.model

import com.google.firebase.Timestamp

open class Notification(val type: Int)

class LikeNotification(
    var postID: String,
    var UID: String,
//    var likeNumbers: Int,
    var date: Timestamp,
//    var notificationMessage: String,
//    var profileName: String
) : Notification(1)

class FriendRequestNotification(
    var UID: String? = null,
    var date: Timestamp,
) : Notification(2)