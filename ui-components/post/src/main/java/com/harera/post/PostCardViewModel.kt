package com.harera.feed.post

import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.harera.model.modelget.Post
import com.harera.model.modelset.Like
import com.harera.post.PostState
import com.harera.repository.db.network.abstract_.AuthManager
import com.harera.repository.db.network.abstract_.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import com.harera.model.modelset.Comment as CommentSet

class PostCardViewModel constructor(
    private val postRepository: PostRepository,
    private val authManager: AuthManager,
) : ViewModel() {
    val postState = MutableStateFlow<PostState>(PostState.Idle)

    fun likeClicked(post: Post, postId: String) {
        postRepository
            .getPostLike(
                postId = postId,
                uid = authManager.getCurrentUser()!!.uid
            )
            .addOnSuccessListener {
                if (it.documents.isNullOrEmpty())
                    addLike(post, postId = postId)
                else
                    removeLike(post, likeId = it.first().id)
            }
    }

    private fun addLike(post: Post, postId: String) {
        postRepository
            .addLike(
                Like(
                    uid = authManager.getCurrentUser()!!.uid,
                    postId = postId,
                    time = Timestamp.now(),
                )
            ).addOnSuccessListener {
                post.likesNumber++
                postState.value = PostState.Idle
                postState.value = PostState.Changed
            }
    }

    private fun removeLike(post: Post, likeId: String) {
        postRepository
            .removeLike(likeId = likeId)
            .addOnSuccessListener {
                post.likesNumber--
                postState.value = PostState.Idle
                postState.value = PostState.Changed
            }
    }

    fun addComment(post: Post, comment: String, postId: String) {
        postRepository
            .addComment(
                CommentSet(
                    uid = authManager.getCurrentUser()!!.uid,
                    postId = postId,
                    comment = comment,
                    time = Timestamp.now()
                )
            )
            .addOnSuccessListener {
                post.commentsNumber++
                postState.value = PostState.Idle
                postState.value = PostState.Changed
            }
    }
}