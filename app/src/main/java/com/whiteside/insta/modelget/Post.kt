package com.whiteside.insta.modelget

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
    lateinit var likesNumber: String
    lateinit var commentsNumber: String
}
