package com.whiteside.insta.modelset

import com.google.firebase.Timestamp
import com.whiteside.insta.modelget.Notification

data class FollowRequest(
    val fromUid: String,
    val toUid: String,
    val time: Timestamp,
    var id: String? = null
) : Notification(2)
