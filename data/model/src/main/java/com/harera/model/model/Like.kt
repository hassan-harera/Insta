package com.harera.model.model

import com.example.utils.TimeFormatter
import org.joda.time.DateTime

data class Like(
    val postId: Int,
    var username: String? = null,
    var time: String = DateTime().let { TimeFormatter.dateTimeToString(it) },
)