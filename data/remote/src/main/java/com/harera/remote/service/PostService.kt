package com.harera.remote.service

import com.harera.model.model.Comment
import com.harera.model.request.CommentRequest
import com.harera.model.request.LikeRequest
import com.harera.model.response.PostResponse
import com.harera.remote.Routing
import com.harera.remote.URL
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.http.ContentType.Application.Json
import io.ktor.util.*
import java.io.File

interface PostService {
    suspend fun getFeedPosts(token: String): List<PostResponse>
    suspend fun getPost(token: String, postId: Int): PostResponse
    suspend fun addPost(token: String, caption: String, image: File): String
    suspend fun getProfilePosts(token: String): List<PostResponse>
    suspend fun getUserPosts(username: String, token: String): List<PostResponse>
    suspend fun insertComment(commentRequest: CommentRequest, token: String): String
    suspend fun insertLike(likeRequest: LikeRequest, token: String): String
    suspend fun getPostComments(postId: Int, token: String): List<Comment>
}

@InternalAPI
class PostServiceImpl(private val client: HttpClient) : PostService {

    override suspend fun getFeedPosts(token: String) =
        client.get<List<PostResponse>>(URL.BASE_URL.plus(Routing.FEED)) {
            header(HttpHeaders.Authorization, "Bearer ".plus(token))
        }

    override suspend fun getPost(token: String, postId: Int) =
        client.get<PostResponse>(URL.BASE_URL.plus(Routing.POST).plus(postId)) {
            header(HttpHeaders.Authorization, "Bearer ".plus(token))
        }

    override suspend fun addPost(token: String, caption: String, image: File) =
        client.post<String>(URL.BASE_URL.plus("post")) {
            header(HttpHeaders.Authorization, "Bearer ".plus(token))
            formData {
                append("image", image, Headers.build {
                    append(HttpHeaders.ContentType, "multipart/form-data; boundary=something")
                })

                append(
                    FormPart(key = "caption", value = caption),
                )

                append(
                    FormPart(key = "image", value = image),
                )
            }.let {
                body = MultiPartFormDataContent(it)
            }
        }

    override suspend fun getProfilePosts(token: String): List<PostResponse> =
        client.get<List<PostResponse>> {
            url(Routing.GET_PROFILE_POSTS)
            header(HttpHeaders.Authorization, "Bearer ".plus(token))
        }

    override suspend fun getUserPosts(username: String, token: String): List<PostResponse> =
        client.get<List<PostResponse>> {
            url(URL.BASE_URL.plus("user/$username/posts"))
            header(HttpHeaders.Authorization, "Bearer ".plus(token))
        }

    override suspend fun insertComment(commentRequest: CommentRequest, token: String) =
        client.post<String> {
            url(URL.BASE_URL.plus("/comment"))
            header(HttpHeaders.Authorization, "Bearer ".plus(token))
            contentType(Json)
            body = commentRequest
        }

    override suspend fun insertLike(likeRequest: LikeRequest, token: String) : String =
        client.post<String> {
            url(URL.BASE_URL.plus("/like"))
            header(HttpHeaders.Authorization, "Bearer ".plus(token))
            contentType(Json)
            body = likeRequest
        }

    override suspend fun getPostComments(postId: Int, token: String) : List<Comment> =
        client.get<List<Comment>> {
            url(URL.BASE_URL.plus("posts/$postId/comments"))
            header(HttpHeaders.Authorization, "Bearer ".plus(token))
            contentType(Json)
        }
}