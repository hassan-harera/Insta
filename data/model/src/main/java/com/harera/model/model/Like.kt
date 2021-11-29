package com.harera.model.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.harera.time.TimeFormatter
import kotlinx.serialization.Serializable
import org.joda.time.DateTime

@Serializable
@Entity
data class Like(
    @PrimaryKey
    val postId: Int,
    var username: String? = null,
    var time: String = DateTime().let { TimeFormatter.dateTimeToString(it) },
)