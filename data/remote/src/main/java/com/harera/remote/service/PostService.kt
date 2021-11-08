package com.harera.remote.service

import com.harera.model.response.PostResponse
import com.harera.remote.Routing
import com.harera.remote.URL
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import java.io.File

interface PostService {
    suspend fun getFeedPosts(token: String): List<PostResponse>
    suspend fun getPost(token: String, postId: Int): PostResponse
    suspend fun addPost(token: String, caption: String, image: File): String
}

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
                append(
                    FormPart(key = "caption", value = caption),
                )
                append(
                    FormPart(key = "caption", value = image),
                )
            }
        }
}