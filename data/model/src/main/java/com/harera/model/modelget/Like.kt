package com.harera.model.modelget

import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
class Like : Notification(1) {
    lateinit var profileName: String
    lateinit var postImageUrl: String
    lateinit var postId: String
    lateinit var likeId: String
    lateinit var uid: String
    lateinit var time: Timestamp
}