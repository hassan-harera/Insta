package com.harera.remote.repository

import com.harera.model.model.Comment
import com.harera.model.model.Like
import com.harera.model.model.Post
import com.harera.model.request.CommentRequest
import com.harera.model.request.LikeRequest
import com.harera.model.response.PostResponse
import com.harera.remote.service.PostService
import com.harera.repository.PostRepository
import java.io.File

class PostRepositoryImpl(
    private val postService : PostService
) : PostRepository {

    override suspend fun getFeedPosts(token: String): Result<List<PostResponse>> = kotlin.runCatching {
        postService.getFeedPosts(token)
    }

    override suspend fun insertPost(token : String, caption: String, image: File) = kotlin.runCatching {
        postService.addPost(token = token, caption, image)
    }

    override suspend fun getPost(token : String, postId: Int) = kotlin.runCatching {
        postService.getPost(token, postId)
    }

    override suspend fun getPostLikes(postId: Int): List<Like> {
        TODO("Not yet implemented")
    }

    override suspend fun getPostComments(postId: Int): List<Comment> {
        TODO("Not yet implemented")
    }

    override suspend fun updatePost(post: Post): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun searchPosts(searchWord: String): Result<List<Post>> {
        TODO("Not yet implemented")
    }

    override suspend fun likePost(like: LikeRequest): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun unlikePost(likeRequest: LikeRequest): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun commentPost(comment: CommentRequest): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun checkPostLike(postId: Int, uid: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserPosts(uid: String, limit: Int): Result<List<Post>> {
        TODO("Not yet implemented")
    }
}