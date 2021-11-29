package com.harera.local

import androidx.room.Dao
import androidx.room.Insert
import com.harera.model.model.Comment
import com.harera.model.model.Like
import com.harera.model.model.Post
import com.harera.model.model.User
import com.harera.model.response.ChatResponse
import com.harera.model.response.Connection

@Dao
abstract class InstaDao {

    @Insert
    abstract fun insertPost(post: Post)

    @Insert
    abstract fun insertUser(user: User)

    @Insert
    abstract fun insertConnection(connection: Connection)

    @Insert
    abstract fun insertLike(like: Like)

    @Insert
    abstract fun insertComment(comment: Comment)

    @Insert
    abstract fun insertChat(chatResponse: ChatResponse)
}