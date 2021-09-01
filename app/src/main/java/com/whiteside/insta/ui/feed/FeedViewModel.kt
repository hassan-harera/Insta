package com.whiteside.insta.ui.feed

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Tasks
import com.whiteside.insta.db.network.abstract_.AuthManager
import com.whiteside.insta.db.network.abstract_.PostRepository
import com.whiteside.insta.db.network.abstract_.ProfileRepository
import com.whiteside.insta.modelget.FollowRelation
import com.whiteside.insta.modelget.Post
import com.whiteside.insta.modelget.Profile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val profileRepository: ProfileRepository,
    private val authManager: AuthManager,
) : ViewModel() {
    val posts = mutableStateOf<List<Post>>(emptyList())
    var followings = mutableStateOf<List<String>>(emptyList())

    internal fun getFollowings() {
        profileRepository
            .getFollowings(authManager.getCurrentUser()!!.uid)
            .addOnSuccessListener {
                it.documents.map {
                    it.toObject(FollowRelation::class.java)!!.followingUid
                }.let {
                    followings.value = (it)
                }
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }

    fun getPosts() {
        if (followings.value.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                Tasks.await(postRepository.getFeedPosts(followings = followings.value))
                    .documents.map {
                        it.toObject(Post::class.java)!!
                    }.map { post ->
                        viewModelScope.async(Dispatchers.IO) {
                            val profile = getPostProfile(post.uid)
                            val likes = getPostLikes(post.postId)
                            val comments = getPostComments(post.postId)

                            post.likesNumber = likes.documents.size.toString()
                            post.commentsNumber = comments.documents.size.toString()

                            post.profileImageUrl = profile
                                .getString(Profile::profileImageUrl.name)!!
                            post.profileName = profile.getString(Profile::name.name)!!

                            post
                        }.await()
                    }.let {
                        posts.value = it
                    }
            }
        }
    }

    private suspend fun getPostProfile(uid: String) =
        viewModelScope.async(Dispatchers.IO) {
            Tasks.await(profileRepository.getProfile(uid))
        }.await()

    private suspend fun getPostLikes(postId: String) =
        viewModelScope.async(Dispatchers.IO) {
            Tasks.await(
                postRepository.getPostLikes(postId)
            )
        }.await()

    private suspend fun getPostComments(postId: String) =
        viewModelScope.async(Dispatchers.IO) {
            Tasks.await(postRepository.getPostComments(postId))
        }.await()
}
