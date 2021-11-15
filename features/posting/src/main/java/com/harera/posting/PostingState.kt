package com.harera.posting

import com.harera.base.state.BaseState

sealed class PostingState : BaseState() {
    object Idle : PostingState()
    object Loading : PostingState()
    data class Error(val message: String?) : PostingState()
    data class PostingCompleted(val postId: Int) : PostingState()
}