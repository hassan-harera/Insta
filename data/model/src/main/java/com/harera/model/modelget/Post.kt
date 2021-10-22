package com.harera.model.modelget

import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
class Post {
    lateinit var postImageUrl: String
    lateinit var time: Timestamp
    lateinit var caption: String
    lateinit var postId: String
    lateinit var uid: String
    lateinit var profileName: String
    lateinit var profileImageUrl: String
    var likesNumber: Int = 0
    var commentsNumber: Int = 0
}
