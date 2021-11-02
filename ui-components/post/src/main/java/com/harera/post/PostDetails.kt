package com.harera.post

import androidx.room.PrimaryKey
import com.harera.model.model.Post
import java.util.*

class PostDetails : Post {
    lateinit var profileName: String
    lateinit var profileImageUrl: String
    var likesNumber = 0
    var commentsNumber = 0
    @PrimaryKey(autoGenerate = true)
    var id : Int = 1

    constructor(
        postImageUrl: String,
        time: Date,
        caption: String,
        postId: String,
        uid: String,
        profileName: String,
        profileImageUrl: String,
        likesNumber: Int,
        commentsNumber: Int
    ) {
        this.postImageUrl = postImageUrl
        this.time = time
        this.caption = caption
        this.postId = postId
        this.uid = uid
        this.profileName = profileName
        this.profileImageUrl = profileImageUrl
        this.likesNumber = likesNumber
        this.commentsNumber = commentsNumber
    }

    constructor() {}
}