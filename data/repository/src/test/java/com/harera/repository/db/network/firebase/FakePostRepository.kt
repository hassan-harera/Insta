package com.harera.repository.db.network.firebase

import android.graphics.Bitmap
import com.google.firebase.firestore.SetOptions
import com.harera.model.modelset.Comment
import com.harera.model.modelset.Like
import com.harera.model.modelset.Post
import com.harera.repository.common.Constansts.COMMENTS
import com.harera.repository.common.Constansts.LIKES
import com.harera.repository.common.Constansts.POSTS
import com.harera.repository.common.Constansts.USERS
import com.harera.repository.db.network.abstract_.PostRepository
import java.io.ByteArrayOutputStream
import com.harera.model.modelget.Comment as CommentGet
import com.harera.model.modelget.Like as LikeGet
import com.harera.model.modelget.Post as PostGet


class FakePostRepository : PostRepository {
    private val feedPosts : MutableList<PostGet>()
    private val posts = ArrayList<Post>()
    private val likes = ArrayList<Like>()

    override suspend fun getFeedPosts(followings: List<String>, limit: Int): List<PostGet> =
        feedPosts

    override suspend fun getPost(postId: String) =
        try {
            feedPosts.singleOrNull {
                it.postId == postId
            }
        } catch (e: Exception) {
            throw e
        }


    override suspend fun getUserPosts(uid: String, limit: Int): List<PostGet> =
        posts

    override suspend fun updatePost(post: Post): Boolean =
        try {
            feedPosts.singleOrNull {
                it.postId == post.postId
            }.let {
                it?.let {
                    posts.add(it)
                }
                it != null
            }
        } catch (e: Exception) {
            throw e
        }


    override suspend fun addLike(like: Like): Boolean =
        try {
            likes.add(like)
        } catch (e: Exception) {
            throw e
        }


    override suspend fun removeLike(likeId: String): Boolean =
        try {
            likes.removeIf {
                it.likeId == likeId
            }
        } catch (e: Exception) {
            throw e
        }


    override suspend fun uploadPostImage(
        imageBitmap: Bitmap,
        postId: String,
        uid: String
    ): Boolean =
        try {
            true
        } catch (e: Exception) {
            throw e
        }

    override suspend fun getPostLikes(postId: String): List<PostGet> =
        try {
            fStore.collection(LIKES)
                .whereEqualTo(Post::postId.name, postId)
                .get()
                .result
                .map {
                    it.toObject(PostGet::class.java)
                }
        } catch (e: Exception) {
            throw e
        }

    override suspend fun getPostLike(postId: String, uid: String): LikeGet =
        try {
            fStore.collection(LIKES)
                .whereEqualTo(Like::uid.name, uid)
                .whereEqualTo(Like::postId.name, postId)
                .get()
                .result
                .first()
                .toObject(LikeGet::class.java)
        } catch (e: Exception) {
            throw e
        }


    override suspend fun getPostComments(postId: String): List<CommentGet> =
        try {
            fStore.collection(COMMENTS)
                .whereEqualTo(Post::postId.name, postId)
                .get()
                .result
                .map {
                    it.toObject(CommentGet::class.java)
                }
        } catch (e: Exception) {
            throw e
        }


    override suspend fun addComment(comment: Comment): Boolean =
        try {
            true

        } catch (e: Exception) {
            throw e
        }


    override suspend fun searchPosts(searchWord: String): List<PostGet> =
        try {
            fStore.collection(POSTS)
                .whereGreaterThanOrEqualTo(Post::caption.name, searchWord)
                .get()
                .result
                .map {
                    it.toObject(PostGet::class.java)
                }
        } catch (e: Exception) {
            throw e
        }


    override suspend fun getNewPostId(uid: String): String =
        try {
            fStore.collection(POSTS)
                .document()
                .id
        } catch (e: Exception) {
            throw e
        }


    override suspend fun addPost(post: Post): Boolean =
        try {
            posts.add(post)
            true
        } catch (e: Exception) {
            throw e
        }

}

