package com.whiteside.insta.modelget

import com.google.firebase.Timestamp

class FollowRequest {
    lateinit var fromUid: String
    lateinit var toUid: String
    lateinit var time: Timestamp
    lateinit var id: String
}

