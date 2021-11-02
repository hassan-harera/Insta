package com.harera.posting

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harera.model.model.Post
import com.harera.repository.db.network.abstract_.AuthManager
import com.harera.repository.db.network.abstract_.PostRepository
import com.harera.repository.db.network.abstract_.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import java.util.*

class PostingViewModel constructor(
    private val postRepository: PostRepository,
    private val profileRepository: ProfileRepository,
    private val authManager: AuthManager,
) : ViewModel() {
    var uid = authManager.getCurrentUser()!!.uid

    var state by mutableStateOf<PostingState>(PostingState.Idle)

    private val intent = Channel<PostingIntent>()
    suspend fun sendIntent(intent: PostingIntent) {
        this.intent.send(intent)
    }

    init {
        triggerIntent()
    }

    private fun triggerIntent() {
        viewModelScope.launch(Dispatchers.IO) {
            intent.consumeAsFlow().collect {
                when (it) {
                    is PostingIntent.Post -> {
                        addPost(it.caption, it.imageUri)
                    }
                }
            }
        }
    }

    private suspend fun uploadPostImage(postId: String, imageUri: Uri, caption: String) {
        postRepository.uploadPostImage(
            postId = postId,
            imageUri = imageUri,
            uid = uid
        ).onSuccess {
            uploadPost(postId = postId, caption = caption, postImageUrl = it)
        }.onFailure {
            state = PostingState.Error(it.message)
        }
    }

    private suspend fun addPost(caption: String, imageUri: Uri) {
        getPostId(caption, imageUri)
    }

    private suspend fun getPostId(caption: String, imageUri: Uri) {
        postRepository
            .getNewPostId(uid)
            .onSuccess {
                uploadPostImage(postId = it, imageUri = imageUri, caption = caption)
            }.onFailure {
                state = PostingState.Error(it.message)
            }
    }

    private suspend fun uploadPost(postId: String, caption: String, postImageUrl: String) {
        postRepository.addPost(
            Post().apply {
                this.postId = postId
                this.time = Date()
                this.uid = this@PostingViewModel.uid
                this.caption = caption
                this.postImageUrl = postImageUrl
            }
        ).onSuccess {
            state = PostingState.PostingCompleted(postId = postId)
        }.onFailure {
            state = PostingState.Error(it.message)
            throw it
        }
    }
}
