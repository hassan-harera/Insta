package com.harera.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.harera.model.model.Comment
import com.harera.model.model.Like
import com.harera.model.model.Post
import com.harera.model.model.User
import com.harera.model.response.ChatResponse
import com.harera.model.response.Connection
import com.harera.model.response.MessageResponse

@Database(
    version = 1,
    entities = [
        Post::class,
        Comment::class,
        Like::class,
        User::class,
        Connection::class,
        ChatResponse::class,
        MessageResponse::class,
    ]
)
abstract class InstaDatabase : RoomDatabase() {
    abstract fun getDao(): InstaDao
}