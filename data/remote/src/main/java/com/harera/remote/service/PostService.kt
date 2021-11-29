package com.harera.remote.service

import com.harera.model.model.Comment
import com.harera.model.model.Like
import com.harera.model.request.CommentRequest
import com.harera.model.request.LikeRequest
import com.harera.model.response.PostResponse
import com.harera.remote.Routing
import com.harera.remote.URL
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.ContentType.Application.Json
import io.ktor.util.*
import okhttp3.*
import java.io.File
import java.lang.Exception


interface PostService {
    suspend fun getFeedPosts(token: String, page: Int): List<PostResponse>
    suspend fun getPost(token: String, postId: Int): PostResponse
    suspend fun insertImagePost(token: String, caption: String, image: File): String
    suspend fun getProfilePosts(token: String): List<PostResponse>
    suspend fun getUserPosts(username: String, token: String): List<PostResponse>
    suspend fun insertComment(commentRequest: CommentRequest, token: String): String
    suspend fun insertLike(likeRequest: LikeRequest, token: String): String
    suspend fun getPostComments(postId: Int, token: String): List<Comment>
    suspend fun getPostLikes(postId: Int, token: String): List<Like>
    suspend fun insertTextPost(token: String, caption: String, image: File): String
}

@InternalAPI
class PostServiceImpl(private val client: HttpClient) : PostService {

    private val TAG = "PostService"

    override suspend fun getFeedPosts(token: String, page: Int) =
        client.get<List<PostResponse>>(URL.BASE_URL.plus(Routing.FEED)) {
            parameter("page", page)
        }

    override suspend fun getPost(token: String, postId: Int) =
        client.get<PostResponse>(URL.BASE_URL.plus(Routing.POST).plus(postId))

    override suspend fun insertImagePost(token: String, caption: String, image: File): String {
        val client: OkHttpClient = OkHttpClient().newBuilder().build()

        val body = MultipartBody
            .Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "image",
                "image.png",
                RequestBody.create(MediaType.parse("application/octet-stream"), image)
            )
            .addFormDataPart("caption", caption)
            .addFormDataPart("type", "1")
            .build()

        val request: Request = Request.Builder()
            .url(URL.BASE_URL.plus("/post"))
            .post(body)
            .addHeader(
                "Authorization",
                "Bearer $token"
            )
            .build()

        val response = client.newCall(request).execute()
        return response.body()?.string() ?: throw Exception()
    }

    override suspend fun insertTextPost(token: String, caption: String, image: File): String {
        val client: OkHttpClient = OkHttpClient().newBuilder().build()

        val body = MultipartBody
            .Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "image",
                "image.png",
                RequestBody.create(MediaType.parse("application/octet-stream"), image)
            )
            .addFormDataPart("caption", caption)
            .addFormDataPart("type", "2")
            .build()

        val request: Request = Request.Builder()
            .url(URL.BASE_URL.plus("/post"))
            .post(body)
            .addHeader(
                "Authorization",
                "Bearer $token"
            )
            .build()

        val response = client.newCall(request).execute()
        return response.body()?.string() ?: throw Exception()
    }

    override suspend fun getProfilePosts(token: String): List<PostResponse> =
        client.get<List<PostResponse>> {
            url(Routing.GET_PROFILE_POSTS)
            
        }

    override suspend fun getUserPosts(username: String, token: String): List<PostResponse> =
        client.get<List<PostResponse>> {
            url(URL.BASE_URL.plus("user/$username/posts"))
            
        }

    override suspend fun insertComment(commentRequest: CommentRequest, token: String) =
        client.post<String> {
            url(URL.BASE_URL.plus("/comment"))
            
            contentType(Json)
            body = commentRequest
        }

    override suspend fun insertLike(likeRequest: LikeRequest, token: String): String =
        client.post<String> {
            url(URL.BASE_URL.plus("/like"))
            
            contentType(Json)
            body = likeRequest
        }

    override suspend fun getPostComments(postId: Int, token: String): List<Comment> =
        client.get<List<Comment>> {
            url(URL.BASE_URL.plus("posts/$postId/comments"))
            
            contentType(Json)
        }

    override suspend fun getPostLikes(postId: Int, token: String): List<Like> =
        client.get<List<Like>> {
            url(URL.BASE_URL.plus("posts/$postId/likes"))
            
        }
}