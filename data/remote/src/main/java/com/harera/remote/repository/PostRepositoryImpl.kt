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
    private val postService: PostService,
) : PostRepository {

    override suspend fun getFeedPosts(token: String): Result<List<PostResponse>> =
        kotlin.runCatching {
            postService.getFeedPosts(token)
        }

    override suspend fun insertPost(token: String, caption: String, image: File) =
        kotlin.runCatching {
            postService.addPost(token = token, caption, image)
        }

    override suspend fun getProfilePosts(token: String): Result<List<PostResponse>> =
        kotlin.runCatching {
            postService.getProfilePosts(token)
        }

    override suspend fun getPost(token: String, postId: Int) = kotlin.runCatching {
        postService.getPost(token, postId)
    }

    override suspend fun getPostLikes(postId: Int): List<Like> {
        TODO("Not yet implemented")
    }

    override suspend fun getPostComments(token: String, postId: Int): Result<List<Comment>> = kotlin.runCatching {
        postService.getPostComments(postId = postId, token = token)
    }
    override suspend fun updatePost(post: Post): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun searchPosts(searchWord: String): Result<List<Post>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertLike(like: LikeRequest, token: String): Result<String> =
        kotlin.runCatching {
            postService.insertLike(likeRequest = like, token = token)
        }

    override suspend fun unlikePost(likeRequest: LikeRequest): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun checkPostLike(postId: Int, uid: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun insertPost(postId: Int, comment: String, token: String): Result<String> =
        kotlin.runCatching {
            postService.insertComment(
                CommentRequest(
                    postId = postId,
                    comment = comment
                ),
                token
            )
        }

    override suspend fun getUserPosts(username: String, token: String): Result<List<PostResponse>> =
        kotlin.runCatching {
            postService.getUserPosts(username, token)
        }
}
