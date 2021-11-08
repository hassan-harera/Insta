package com.harera.post

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.harera.base.base.BaseViewModel
import com.harera.base.datastore.UserSharedPreferences
import com.harera.base.state.PostState
import com.harera.model.request.CommentRequest
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
    userSharedPreferences: UserSharedPreferences,
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
                    checkLike(it.postId)
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
                state = PostState.Error(it.message)
            }
    }

    private suspend fun getComments(postId: Int) {
        postRepository.getPostComments(postId)
            .map { comment ->
                profileRepository.getProfile(comment.username).let {
//                    TODO
//                    comment.profileName = it.name
                }
                comment
            }
            .let {
                state = PostState.CommentsFetched(it)
            }
    }

    private suspend fun getPostLikes(postId: Int) =
        postRepository
            .getPostLikes(postId)

    private suspend fun getPostCommentsTask(postId: Int) =
        postRepository
            .getPostComments(postId)

    private suspend fun addComment(comment: String, postId: Int) {
        val commentRequest = CommentRequest(
            postId = 123,
            comment = comment
        )

        postRepository
            .commentPost(comment = commentRequest)
            .onSuccess {
                getComments(postId)
            }.onFailure {
                state = PostState.Error(it.message)
            }
    }

    private suspend fun checkLike(postId: Int) {
        postRepository
            .checkPostLike(
                postId = postId,
                uid = token!!,
            )
            .onSuccess {
                if (it == null)
                    addLike(postId = postId)
                else
                    removeLike(postId)
            }.onFailure {
                state = PostState.Error(it.message)
            }
    }

    private suspend fun addLike(postId: Int) {
        runCatching {
            postRepository
                .likePost(
                    LikeRequest(
                        postId = postId,
                    )
                )
        }.getOrElse {
            getPostLikes(postId)
        }
    }

    private suspend fun removeLike(postUid: Int) = kotlin.runCatching {
        postRepository
            .unlikePost(likeRequest = LikeRequest(postUid))
            .onSuccess {
                state = PostState.Error("couldn't remove like")
            }
            .onFailure {
                state = PostState.Error("couldn't remove like")
            }
    }

}