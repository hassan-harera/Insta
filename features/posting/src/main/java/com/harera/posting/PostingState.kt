package com.harera.posting

sealed class PostingState {
    object Idle : PostingState()
    object Loading : PostingState()
    data class Error(val message: String?) : PostingState()
    data class PostingCompleted(val postId: String) : PostingState()
}