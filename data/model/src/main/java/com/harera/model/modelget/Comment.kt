package com.harera.model.modelget

import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
class Comment {
    lateinit var uid: String
    lateinit var postId: String
    lateinit var commentId: String
    lateinit var comment: String
    lateinit var time: Timestamp
    lateinit var profileName: String
}
