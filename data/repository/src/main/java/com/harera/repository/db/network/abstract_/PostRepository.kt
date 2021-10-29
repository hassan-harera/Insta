package com.harera.repository.db.network.abstract_

import android.graphics.Bitmap
import android.net.Uri
import com.harera.model.model.Comment
import com.harera.model.model.Like
import com.harera.model.model.Post

interface PostRepository {

    suspend fun getFeedPosts(
        followings: List<String>,
        limit: Int
    ): Result<List<Post>>

    suspend fun getPost(postId: String): Result<Post?>
    suspend fun updatePost(post: Post): Result<Boolean>
    suspend fun searchPosts(searchWord: String): Result<List<Post>>
    suspend fun getNewPostId(uid: String): Result<String>
    suspend fun addPost(post: Post): Result<Boolean>
    suspend fun addLike(like: Like): Result<Boolean>
    suspend fun removeLike(postUid: String, uid: String): Result<Boolean>
    suspend fun uploadPostImage(imageBitmap: Bitmap, postId: String, uid: String): Result<String>
    suspend fun getPostLikes(postId: String): List<Post>
    suspend fun getPostComments(postId: String): List<Comment>
    suspend fun addComment(comment: Comment): Result<Boolean>
    suspend fun getPostLike(postId: String, uid: String): Result<Like?>
    suspend fun getUserPosts(uid: String, limit: Int = 15): Result<List<Post>>
    suspend fun uploadPostImage(imageUri: Uri, postId: String, uid: String): Result<String>
}
