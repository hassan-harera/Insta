package com.harera.model.modelset

import com.google.firebase.Timestamp
import com.harera.model.modelget.Notification

data class FollowRequest(
    val fromUid: String,
    val toUid: String,
    val time: Timestamp,
    var id: String? = null
) : Notification(2)
