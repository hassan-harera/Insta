package com.harera.posting

import com.harera.base.state.State

sealed class PostingState : State() {
    object Idle : PostingState()
    object Loading : PostingState()
    data class Error(val message: String?) : PostingState()
    data class PostingCompleted(val postId: Int) : PostingState()
}