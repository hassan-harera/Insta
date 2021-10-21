package com.harera.profile

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
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val authManager: AuthManager,
    private val postRepository: PostRepository,
) : ViewModel() {
    private var uid = authManager.getCurrentUser()!!.uid

    var intent = Channel<ProfileIntent>()

    private var _state: MutableStateFlow<ProfileState> = MutableStateFlow(ProfileState.Idle)
    val state: StateFlow<ProfileState>
        get() = this._state

    private var _profile: MutableStateFlow<Profile?> = MutableStateFlow(null)
    val profile: StateFlow<Profile?>
        get() = this._profile

    private var _posts: MutableStateFlow<List<Post>> = MutableStateFlow(emptyList())
    val posts: StateFlow<List<Post>>
        get() = this._posts

    init {
        processIntent()
    }

    private fun processIntent() {
        viewModelScope.launch {
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

    private fun getProfile() {
        _state.value = ProfileState.Loading()
        profileRepository
            .getProfile(uid)
            .addOnSuccessListener {
                it.toObject(Profile::class.java)!!.let { profile ->
                    _state.value = ProfileState.ProfilePrepared(profile)
                    _profile.value = profile
                }
            }
            .addOnFailureListener {
                _state.value = ProfileState.Error(it.message)
                it.printStackTrace()
            }
    }

    private fun getPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = (ProfileState.Loading())

            val posts = ArrayList<Post>()

            getUserPosts(uid).forEach { post ->
                withContext(Dispatchers.Default) {
                    getPostDetails(post).join()
                    posts.add(post)
                }

                _state.value = ProfileState.Idle
                _state.value = ProfileState.Posts(emptyList())
                _posts.value = (posts)
            }
        }
    }

    private fun getPostDetails(post: Post) =
        viewModelScope.launch(Dispatchers.IO) {
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
                _state.value = ProfileState.Error(it.message)
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