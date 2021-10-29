package com.harera.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.harera.base.utils.time.TimeConverter
import com.harera.model.model.*

@Database(
    entities = [
        Profile::class,
        Post::class,
        Chat::class,
        Like::class,
        Message::class,
        FollowRelation::class,
        FollowRequest::class,
        OpenChat::class,
    ],
    version = 1,
)
@TypeConverters(TimeConverter::class)
abstract class InstaDatabase : RoomDatabase() {
    abstract fun getDao(): InstaDao
}