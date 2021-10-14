package com.harera.insta.ui.profile.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Tasks
import com.harera.model.modelget.Post
import com.harera.model.modelget.Profile
import com.harera.repository.db.network.abstract_.AuthManager
import com.harera.repository.db.network.abstract_.PostRepository
import com.harera.repository.db.network.abstract_.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    authManager: AuthManager,
    private val postRepository: PostRepository,
) : ViewModel() {

    private val uid = authManager.getCurrentUser()!!.uid
    val profile = mutableStateOf<Profile?>(null)
    val posts = mutableStateOf<List<Post>>(emptyList())

    fun getProfile() {
        profileRepository
            .getProfile(uid)
            .addOnSuccessListener {
                it.toObject(Profile::class.java)!!.let {
                    profile.value = it
                }
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }

    fun getPosts() {
        postRepository
            .getProfilePosts(uid)
            .addOnSuccessListener {
                viewModelScope.launch(Dispatchers.IO) {
                    it.documents.map {
                        it.toObject(Post::class.java)!!
                    }.map { post ->
                        viewModelScope.async(Dispatchers.IO) {
                            val profile = Tasks.await(getPostProfileDetails(post = post))
                            val likes = Tasks.await(getPostLikes(post = post))
                            val comments = Tasks.await(getPostComments(post = post))

                            post.likesNumber = likes.documents.size.toString()
                            post.commentsNumber = comments.documents.size.toString()

                            post.profileImageUrl = profile
                                .getString(Profile::profileImageUrl.name)!!
                            post.profileName = profile.getString(Profile::name.name)!!

                            post
                        }.await()
                    }.let {
                        withContext(Dispatchers.Main) {
                            posts.value = it
                        }
                    }
                }
            }
    }

    private fun getPostProfileDetails(post: Post) =
        profileRepository
            .getProfile(post.uid)

    private fun getPostLikes(post: Post) =
        postRepository
            .getPostLikes(post.postId)

    private fun getPostComments(post: Post) =
        postRepository
            .getPostComments(post.postId)
}