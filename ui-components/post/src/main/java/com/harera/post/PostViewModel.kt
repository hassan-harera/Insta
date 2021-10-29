package com.harera.post

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harera.model.model.Comment
import com.harera.model.model.Like
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

class PostViewModel constructor(
    private val postRepository: PostRepository,
    private val profileRepository: ProfileRepository,
    private val authManager: AuthManager,
) : ViewModel() {

    private val intent = Channel<PostIntent>()
    var state by mutableStateOf<PostState>(PostState.Idle)
        private set

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

    private suspend fun getPost(postId: String) {
        postRepository.getPost(postId)
            .onSuccess { post ->
                getPostDetails(post!!)
            }
            .onFailure {
                state = PostState.Error(it.message)
            }
    }

    private suspend fun getPostDetails(post: Post) {
        val profile = getPostProfileDetails(post = post)
        val likes = getPostLikes(postId = post.postId)
        val comments = getPostCommentsTask(postId = post.postId)

        post.likesNumber = likes.size
        post.commentsNumber = comments.size
        post.profileImageUrl = profile.profileImageUrl
        post.profileName = profile.name

        state = PostState.PostFetched(post)
    }

    private suspend fun getComments(postId: String) {
        postRepository.getPostComments(postId)
            .map { comment ->
                profileRepository.getProfile(comment.uid).let {
                    comment.profileName = it.name
                }
                comment
            }
            .let {
                state = PostState.CommentsFetched(it)
            }
    }

    private suspend fun getPostProfileDetails(post: Post) =
        profileRepository.getProfile(post.uid)

    private suspend fun getPostLikes(postId: String) =
        postRepository
            .getPostLikes(postId)

    private suspend fun getPostCommentsTask(postId: String) =
        postRepository
            .getPostComments(postId)

    private suspend fun addComment(comment: String, postId: String) {
        val commentSet = Comment().apply {
            this.uid = authManager.getCurrentUser()!!.uid
            this.postId = postId
            this.comment = comment
            this.time = Date()
        }

        postRepository
            .addComment(comment = commentSet)
            .onSuccess {
                getComments(postId)
            }.onFailure {
                state = PostState.Error(it.message)
            }
    }

    private suspend fun checkLike(postId: String) {
        postRepository
            .getPostLike(
                postId = postId,
                uid = authManager.getCurrentUser()!!.uid
            )
            .onSuccess {
                if (it == null)
                    addLike(postId = postId)
                else
                    removeLike(postId, uid = authManager.getCurrentUser()!!.uid)
            }.onFailure {
                state = PostState.Error(it.message)
            }
    }

    private suspend fun addLike(postId: String) {
        runCatching {
            postRepository
                .addLike(
                    Like().apply {
                        this.uid = authManager.getCurrentUser()!!.uid
                        this.postId = postId
                        this.time = Date()
                    }
                )
        }.getOrElse {
            getPostLikes(postId)
        }
    }

    private suspend fun removeLike(postUid: String, uid: String) = kotlin.runCatching {
        postRepository
            .removeLike(postUid = postUid, uid = uid)
            .onSuccess {
                state = PostState.Error("couldn't remove like")
            }
            .onFailure {
                state = PostState.Error("couldn't remove like")
            }
    }

}