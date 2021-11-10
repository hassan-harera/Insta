package com.harera.posting

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.harera.base.base.BaseViewModel
import com.harera.base.datastore.LocalStore
import com.harera.repository.PostRepository
import com.harera.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import java.io.File

class PostingViewModel constructor(
    private val postRepository: PostRepository,
    private val profileRepository: ProfileRepository,
    userSharedPreferences: LocalStore,
) : BaseViewModel<PostingState>(userSharedPreferences) {

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

    private suspend fun addPost(caption: String, imageUri: Uri) {
        postRepository.insertPost(
            token = token!!,
            caption = caption,
            image = File(imageUri.path!!)
        ).onSuccess {
//            state = PostingState.PostingCompleted(postId = postId.toInt())
        }.onFailure {
            handleFailure(it)
            state = PostingState.Error(it.message)
        }
    }
}
