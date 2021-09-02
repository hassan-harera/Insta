package com.whiteside.insta.ui.profile.visit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Tasks
import com.whiteside.insta.db.network.abstract_.AuthManager
import com.whiteside.insta.db.network.abstract_.PostRepository
import com.whiteside.insta.db.network.abstract_.ProfileRepository
import com.whiteside.insta.modelget.Post
import com.whiteside.insta.modelget.Profile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class VisitProfileViewModel @Inject constructor(
    private val authManager: AuthManager,
    private val profileRepository: ProfileRepository,
    private val postRepository: PostRepository,
) : ViewModel() {

    var followButtonState = MutableLiveData(false)
    private lateinit var uid: String
    val profile = MutableLiveData<Profile>()
    val posts = MutableLiveData<List<Post>>(emptyList())

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

    fun getFollowButtonState() {
        profileRepository
            .getFollower(uid, authManager.getCurrentUser()!!.uid)
            .addOnSuccessListener {
                followButtonState.value =
                    (uid) != authManager.getCurrentUser()!!.uid && (it.documents.isNullOrEmpty())
            }
    }

    fun setUid(uid: String) {
        this.uid = uid
    }
}