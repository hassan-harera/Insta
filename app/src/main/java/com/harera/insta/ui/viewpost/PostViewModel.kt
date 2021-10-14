package com.harera.insta.ui.viewpost

import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Timestamp
import com.harera.model.modelget.Comment
import com.harera.model.modelget.Post
import com.harera.model.modelget.Profile
import com.harera.model.modelset.Like
import com.harera.repository.db.network.abstract_.AuthManager
import com.harera.repository.db.network.abstract_.PostRepository
import com.harera.repository.db.network.abstract_.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.harera.model.modelset.Comment as CommentSet
import com.harera.model.modelset.Post as PostSet

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val profileRepository: ProfileRepository,
    private val authManager: AuthManager,
) : ViewModel() {
    val searchWord = mutableStateOf("")
    val profile = mutableStateOf<Profile?>(null)
    val post = mutableStateOf<Post?>(null)
    val postComments = mutableStateOf<List<Comment>>(emptyList())

    val loading = mutableStateOf(true)
    val postLiked = MutableLiveData(false)

    fun getPost(postId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Tasks.await(postRepository.getPost(postId))
                .toObject(Post::class.java)!!.let { _post ->
                    val profile = Tasks.await(getPostProfileDetails(post = _post))
                    val likes = Tasks.await(getPostLikes(post = _post))
                    val comments = Tasks.await(getPostCommentsTask(postId = _post.postId))

                    _post.likesNumber = likes.documents.size.toString()
                    _post.commentsNumber = comments.documents.size.toString()
                    _post.profileImageUrl = profile
                        .getString(Profile::profileImageUrl.name)!!
                    _post.profileName = profile.getString(Profile::name.name)!!

                    withContext(Dispatchers.Main) {
                        post.value = _post
                    }
                }
        }
    }
    fun getComments(postId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Tasks.await(postRepository.getPostComments(postId))
                .documents.map {
                    it.toObject(Comment::class.java)!!.let { comment ->
                        Tasks.await(profileRepository.getProfile(comment.uid)).let {
                            comment.profileName = it.getString(Profile::name.name)!!
                        }
                        return@map comment
                    }
                }.let {
                    withContext(Dispatchers.Main) {
                        postComments.value = it
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

    private fun getPostCommentsTask(postId: String) =
        postRepository
            .getPostComments(postId)

    fun checkPostLiked(postId: String) {
        postRepository
            .getPostLike(postId = postId, uid = authManager.getCurrentUser()!!.uid)
            .addOnSuccessListener {
                postLiked.value = !it.documents.isNullOrEmpty()
            }
    }

    fun likeClicked(postId: String) {
        postRepository
            .getPostLike(
                postId = postId,
                uid = authManager.getCurrentUser()!!.uid
            )
            .addOnSuccessListener {
                if (it.documents.isNullOrEmpty())
                    addLike(postId = postId)
                else
                    removeLike(likeId = it.first().id)
            }
    }

    private fun addLike(postId: String) {
        postRepository
            .addLike(
                Like(
                    uid = authManager.getCurrentUser()!!.uid,
                    postId = postId,
                    time = Timestamp.now(),
                )
            )
    }

    private fun removeLike(likeId: String) {
        postRepository
            .removeLike(likeId = likeId)
    }

    fun addComment(comment: String, postId: String) {
        postRepository
            .addComment(
                CommentSet(
                    uid = authManager.getCurrentUser()!!.uid,
                    postId = postId,
                    comment = comment,
                    time = Timestamp.now()
                )
            )
    }

    fun search() {
    }

    fun addPost(caption: String, imageBitmap: Bitmap) {
        val postId = postRepository
            .getNewPostId(authManager.getCurrentUser()!!.uid)

        viewModelScope.launch(Dispatchers.IO) {
            val reference =
                postRepository.uploadPostImage(
                    postId = postId,
                    imageBitmap = imageBitmap,
                    uid = authManager.getCurrentUser()!!.uid
                ).let {
                    Tasks.await(it)
                }

            val uri = reference.storage.downloadUrl.let {
                Tasks.await(it)
            }

            uri.toString().let { postImageUrl ->
                postRepository.addPost(
                    PostSet(
                        postId = postId,
                        time = Timestamp.now(),
                        uid = authManager.getCurrentUser()!!.uid,
                        caption = caption,
                        postImageUrl = postImageUrl,
                    )
                )
            }
        }
    }
}