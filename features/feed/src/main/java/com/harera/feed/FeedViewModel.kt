package com.harera.feed

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Tasks
import com.harera.model.modelget.FollowRelation
import com.harera.model.modelget.Post
import com.harera.model.modelget.Profile
import com.harera.repository.db.network.abstract_.AuthManager
import com.harera.repository.db.network.abstract_.PostRepository
import com.harera.repository.db.network.abstract_.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val profileRepository: ProfileRepository,
    private val authManager: AuthManager,
) : ViewModel() {
    var intent = Channel<FeedIntent>()
    private var _state: MutableStateFlow<FeedState> = MutableStateFlow(FeedState.Idle)
    val state: StateFlow<FeedState>
        get() = this._state

    init {
        processIntent()
    }

    private fun processIntent() {
        viewModelScope.launch {
            intent.consumeAsFlow().collect { intent ->
                when (intent) {
                    is FeedIntent.FetchPosts -> {
                        getPosts()
                    }
                }
            }
        }
    }

    private suspend fun getFollowings() =
        viewModelScope.async(Dispatchers.IO) {

            val task = profileRepository.getFollowings(authManager.getCurrentUser()!!.uid)

            Tasks.await(task)

            if (task.isSuccessful)
                Tasks.await(task)
                    .documents.map {
                        it.get(FollowRelation::followingUid.name, String::class.java)!!
                    }.let {
                        return@async it
                    }

            task.exception?.let {
                this@FeedViewModel._state.value = (FeedState.Error(it.message))
            }

            emptyList()
        }.await()

    private fun getPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            this@FeedViewModel._state.value = (FeedState.Loading())

            val posts = ArrayList<Post>()

            getFollowings().forEach { uid ->
                getUserPosts(uid).forEach { post ->
                    Log.d("getPostDetails", "getPostDetails")
                    withContext(Dispatchers.Default) {
                        getPostDetails(post).join()
                        Log.d("after getPostDetails", "after getPostDetails")
                        posts.add(post)
                    }
                    this@FeedViewModel._state.value = (FeedState.Idle)
                    this@FeedViewModel._state.value = (FeedState.Posts(posts))
                }
            }
        }
    }

    private fun getPostDetails(post: Post) =
        viewModelScope.launch(Dispatchers.IO) {
            getPostProfile(post.uid).let { profile ->
                Log.d("getPostDetails", profile.profileImageUrl)
                post.profileImageUrl = profile.profileImageUrl
                post.profileName = profile.name
            }

            getPostNumbers(post.postId).let { likes ->
                post.likesNumber = likes.toString()
            }

            getPostCommentNumbers(post.postId).let { size ->
                post.commentsNumber = size.toString()
            }
        }

    private suspend fun getUserPosts(uid: String) =
        viewModelScope.async(Dispatchers.IO) {
            val task = postRepository.getUserPosts(uid)
            Tasks.await(task)

            if (task.isSuccessful)
                Tasks.await(task)
                    .documents.map {
                        it.toObject(Post::class.java)!!
                    }.let {
                        return@async it
                    }

            task.exception?.let {
                this@FeedViewModel._state.value = (FeedState.Error(it.message))
            }

            emptyList()
        }.await()

    private suspend fun getPostProfile(uid: String): Profile =
        viewModelScope.async(Dispatchers.IO) {
            Tasks.await(profileRepository.getProfile(uid)).toObject(Profile::class.java)!!
        }.await()

    private suspend fun getPostNumbers(postId: String): Int = viewModelScope.async(Dispatchers.IO) {
        Tasks.await(postRepository.getPostLikes(postId)).documents.size
    }.await()

    private suspend fun getPostCommentNumbers(postId: String): Int =
        viewModelScope.async(Dispatchers.IO) {
            Tasks.await(postRepository.getPostComments(postId)).documents.size
        }.await()

}