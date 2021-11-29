package com.harera.remote.service

import com.harera.model.response.SearchedPost
import com.harera.remote.URL
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

interface SearchService {
    suspend fun searchPosts(text : String) : List<SearchedPost>
}

class SearchServiceImpl(
    private val client: HttpClient,
) : SearchService {
    override suspend fun searchPosts(text : String): List<SearchedPost> =
        client.get<List<SearchedPost>>(URL.BASE_URL.plus("search/posts?text=$text"))
}