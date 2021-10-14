package com.harera.model.modelget

import com.google.firebase.Timestamp

class FollowRequest : Notification(2) {
    lateinit var fromUid: String
    lateinit var toUid: String
    lateinit var time: Timestamp
    lateinit var id: String
}

