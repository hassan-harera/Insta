package com.harera.profile

import android.content.ContentValues.TAG
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

class HomeProfileViewModel constructor(
    private val profileRepository: ProfileRepository,
    private val authManager: AuthManager,
    private val postRepository: PostRepository,
) : ViewModel() {
    private var uid = authManager.getCurrentUser()!!.uid

    var intent = Channel<ProfileIntent>()

    var state by mutableStateOf<ProfileState>(ProfileState.Idle)
        private set

    init {
        processIntent()
    }

    private fun processIntent() {
        viewModelScope.launch(Dispatchers.IO) {
            intent.consumeAsFlow().collect { intent ->
                when (intent) {
                    is ProfileIntent.GetProfile -> {
                        getProfile()
                    }

                    is ProfileIntent.GetPosts -> {
                        getPosts()
                    }
                }
            }
        }
    }

    private suspend fun getProfile() {
//        state = ProfileState.Loading()

        profileRepository
            .getProfile(uid)
            .let { profile ->
                state = ProfileState.ProfilePrepared(profile)
            }
    }

    private suspend fun getPosts() {
        val posts = ArrayList<Post>()

        postRepository
            .getUserPosts(uid)
            .onSuccess {
                it.forEach { post ->
                    getPostDetails(post).getOrNull()
                    posts.add(post)

                    state = ProfileState.PostsFetched(posts)
                }
            }
            .onFailure {
                state = ProfileState.Loading(it.message)
            }
    }

    private suspend fun getPostDetails(post: Post) = viewModelScope.runCatching {
        getPostProfile(post.uid).let { profile ->
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

    private suspend fun getPostProfile(uid: String): Profile =
        profileRepository.getProfile(uid)

    private suspend fun getPostNumbers(postId: String): Int =
        postRepository.getPostLikes(postId).size


    private suspend fun getPostCommentNumbers(postId: String): Int =
        postRepository.getPostComments(postId).size

}


