package com.harera.model.response

import kotlinx.serialization.Serializable

@Serializable
abstract class Notification {
    abstract var type: Int
    abstract var time: String
}