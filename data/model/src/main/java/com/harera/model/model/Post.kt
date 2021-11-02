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
open class PostTable : Post() {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id = 0
}

@IgnoreExtraProperties
open class Post {
    lateinit var postId: String
    lateinit var uid: String
    lateinit var postImageUrl: String
    @Serializable(with = InstaDateSerializer::class)
    lateinit var time: Date
    lateinit var caption: String

    @Ignore
    constructor(
        postImageUrl: String,
        time: Date,
        caption: String,
        postId: String,
        uid: String,
    ) {
        this.postImageUrl = postImageUrl
        this.time = time
        this.caption = caption
        this.postId = postId
        this.uid = uid
    }

    constructor() {}
}