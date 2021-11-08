package com.harera.repository

import com.harera.model.model.Comment
import com.harera.model.model.Like
import com.harera.model.model.Post
import com.harera.model.request.CommentRequest
import com.harera.model.request.LikeRequest
import com.harera.model.request.PostRequest
import com.harera.model.response.PostResponse
import java.io.File

interface PostRepository {

    suspend fun getFeedPosts(token: String,): Result<List<PostResponse>>
    suspend fun getPostLikes(postId: Int): List<Like>
    suspend fun getPostComments(postId: Int): List<Comment>
    suspend fun updatePost(post: Post): Result<Boolean>
    suspend fun searchPosts(searchWord: String): Result<List<Post>>
    suspend fun likePost(like: LikeRequest): Result<Boolean>
    suspend fun unlikePost(likeRequest: LikeRequest): Result<Boolean>
    suspend fun commentPost(comment: CommentRequest): Result<Boolean>
    suspend fun checkPostLike(postId: Int, uid: String): Result<Boolean>
    suspend fun getUserPosts(uid: String, limit: Int = 15): Result<List<Post>>
    suspend fun getPost(token: String, postId: Int): Result<PostResponse>
    suspend fun insertPost(token: String, caption: String, image: File): Result<String>
}
