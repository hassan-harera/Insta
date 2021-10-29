package com.harera.model.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.firebase.firestore.IgnoreExtraProperties
import com.harera.base.utils.time.InstaDateSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Entity
@IgnoreExtraProperties
@Serializable(with = InstaDateSerializer::class)
class Post {
    lateinit var postImageUrl: String
    lateinit var time: Date
    lateinit var caption: String

    @PrimaryKey
    @NonNull
    lateinit var postId: String
    lateinit var uid: String
    lateinit var profileName: String
    lateinit var profileImageUrl: String
    var likesNumber = 0
    var commentsNumber = 0

    @Ignore
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