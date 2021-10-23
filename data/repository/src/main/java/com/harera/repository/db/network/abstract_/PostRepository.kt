package com.harera.repository.db.network.abstract_

import android.graphics.Bitmap
import com.harera.model.modelset.Comment
import com.harera.model.modelset.Like
import com.harera.model.modelset.Post

interface PostRepository {

    suspend fun getFeedPosts(
        followings: List<String>,
        limit: Int
    ): List<com.harera.model.modelget.Post>

    suspend fun getPost(postId: String): com.harera.model.modelget.Post?
    suspend fun updatePost(post: Post): Boolean
    suspend fun searchPosts(searchWord: String): List<com.harera.model.modelget.Post>
    suspend fun getNewPostId(uid: String): String
    suspend fun addPost(post: Post): Boolean
    suspend fun addLike(like: Like): Boolean
    suspend fun removeLike(likeId: String): Boolean
    suspend fun uploadPostImage(imageBitmap: Bitmap, postId: String, uid: String): Boolean
    suspend fun getPostLikes(postId: String): List<com.harera.model.modelget.Post>
    suspend fun getPostComments(postId: String): List<com.harera.model.modelget.Comment>
    suspend fun addComment(comment: Comment): Boolean
    suspend fun getPostLike(postId: String, uid: String): com.harera.model.modelget.Like
    suspend fun getUserPosts(uid: String, limit: Int = 5): List<com.harera.model.modelget.Post>
}
