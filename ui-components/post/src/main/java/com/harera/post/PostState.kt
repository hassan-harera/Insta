package com.harera.post

sealed class PostState {
    object Idle : PostState()
    object Changed : PostState()
    data class Error(val message: String) : PostState()
}
