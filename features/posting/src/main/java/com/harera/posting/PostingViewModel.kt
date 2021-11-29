package com.harera.posting

import android.graphics.Bitmap
import androidx.lifecycle.viewModelScope
import com.harera.base.base.BaseViewModel
import com.harera.base.datastore.LocalStore
import com.harera.base.utils.ImageUtils.Companion.convertBitmapToFile
import com.harera.repository.PostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class ImagePosting constructor(
    private val postRepository: PostRepository,
    userSharedPreferences: LocalStore,
) : BaseViewModel<PostingState>(userSharedPreferences) {

    private val TAG = "PostingViewModel"

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
                        addPost(it.caption, it.image)
                    }
                }
            }
        }
    }

    private suspend fun addPost(caption: String, image: Bitmap) {
        postRepository.insertImagePost(
            token = token!!,
            caption = caption,
            image = convertBitmapToFile(image),
        ).onSuccess {
//            state = PostingState.PostingCompleted(postId = postId.toInt())
        }.onFailure {
            handleFailure(it)
            state = PostingState.Error(it.message)
        }
    }
}
