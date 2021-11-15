package com.harera.post

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.harera.base.base.BaseViewModel
import com.harera.base.datastore.LocalStore
import com.harera.base.state.PostState
import com.harera.base.state.BaseState
import com.harera.model.request.LikeRequest
import com.harera.repository.PostRepository
import com.harera.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class PostViewModel constructor(
    private val postRepository: PostRepository,
    private val profileRepository: ProfileRepository,
    userSharedPreferences: LocalStore,
) : BaseViewModel<PostState>(userSharedPreferences) {

    private val intent = Channel<PostIntent>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            triggerIntent()
        }
    }

    private suspend fun triggerIntent() {
        intent.consumeAsFlow().collect {
            when (it) {
                is PostIntent.FetchPost -> {
                    getPost(it.postId)
                }

                is PostIntent.CommentToPost -> {
                    addComment(it.comment, it.postId)
                }

                is PostIntent.LikePost -> {
                    addLike(it.postId)
                }
            }
        }
    }


    suspend fun sendIntent(intent: PostIntent) {
        this.intent.send(intent)
        Log.d("sendIntent", intent::class.java.name)
    }

    private suspend fun getPost(postId: Int) {
        postRepository.getPost(token!!, postId)
            .onSuccess { post ->
                state = PostState.PostFetched(post = post)
            }
            .onFailure {
                handleFailure(it)
                state = PostState.Error(it.message)
            }
    }

    private suspend fun getComments(postId: Int) {
        postRepository
            .getPostComments(token!!, postId)
            .onSuccess {
                state = PostState.CommentsFetched(it)
            }
            .onFailure {
                handleFailure(it)
                state = BaseState.Error(it.message)
            }
    }

    private suspend fun getLikes(postId: Int) {
        postRepository
            .getPostLikes(token!!, postId)
            .onSuccess {
                state = PostState.LikesFetched(it)
            }
            .onFailure {
                handleFailure(it)
                state = BaseState.Error(it.message)
            }
    }


    private suspend fun addComment(comment: String, postId: Int) {
        postRepository
            .insertPost(
                postId = postId,
                comment = comment,
                token = token!!
            )
            .onSuccess {
                getComments(postId)
            }.onFailure {
                handleFailure(it)
                state = PostState.Error(it.message)
            }
    }

    private suspend fun addLike(postId: Int) {
        postRepository
            .insertLike(LikeRequest(postId = postId,),token = token!!)
            .onFailure {
                handleFailure(it)
                state = PostState.Error(it.message)
            }
            .onSuccess {
                getLikes(postId = postId)
            }
    }
}