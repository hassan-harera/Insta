package com.harera.feed

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harera.model.modelget.Post
import com.harera.model.modelget.Profile
import com.harera.repository.db.network.abstract_.AuthManager
import com.harera.repository.db.network.abstract_.PostRepository
import com.harera.repository.db.network.abstract_.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class FeedViewModel constructor(
    private val postRepository: PostRepository,
    private val profileRepository: ProfileRepository,
    private val authManager: AuthManager,
) : ViewModel() {
    var intent = Channel<FeedIntent>()
    var state by mutableStateOf<FeedState>(FeedState.Idle)
        private set

    init {
        processIntent()
    }

    private fun processIntent() {
        viewModelScope.launch(Dispatchers.IO) {
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
        profileRepository.getFollowings(authManager.getCurrentUser()!!.uid).map { it.followingUid }

    private suspend fun getPosts() {
        this.state = FeedState.Loading()

        val posts = ArrayList<Post>()

        val followings = getFollowings()
        followings.forEach { uid ->

            getUserPosts(uid).onFailure {

            }.onSuccess {
                it.forEach { post ->
                    getPostDetails(post)
                    posts.add(post)

                    this.state = (FeedState.Posts(posts))
                }
            }
        }
    }

    private suspend fun getPostDetails(post: Post) {
        getPostProfile(post.uid).let { profile ->
            Log.d("getPostDetails", profile.profileImageUrl)
            post.profileImageUrl = profile.profileImageUrl
            post.profileName = profile.name
        }

        getPostNumbers(post.postId).let { likes ->
            post.likesNumber = likes
        }

        getPostCommentNumbers(post.postId).let { size ->
            post.commentsNumber = size
        }
    }

    private suspend fun getUserPosts(uid: String) =
        postRepository.getUserPosts(uid)

    private suspend fun getPostProfile(uid: String): Profile =
        profileRepository.getProfile(uid)

    private suspend fun getPostNumbers(postId: String): Int =
        postRepository.getPostLikes(postId).size

    private suspend fun getPostCommentNumbers(postId: String): Int =
        postRepository.getPostComments(postId).size
}
