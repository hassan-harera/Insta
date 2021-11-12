package com.harera.model.model

import com.harera.time.TimeFormatter
import kotlinx.serialization.Serializable
import org.joda.time.DateTime

@Serializable
data class Like(
    val postId: Int,
    var username: String? = null,
    var time: String = DateTime().let { TimeFormatter.dateTimeToString(it) },
)