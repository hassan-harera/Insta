package com.whiteside.insta.modelset

import com.google.firebase.Timestamp

data class FollowRelation(
    val followerUid: String,
    val followingUid: String,
    val timestamp: Timestamp,
    var followId: String? = null,
)
