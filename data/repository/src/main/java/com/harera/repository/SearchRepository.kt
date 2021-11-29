package com.harera.repository

import com.harera.model.response.SearchedPost

interface SearchRepository {
    suspend fun searchPosts(text : String) : Result<List<SearchedPost>>
}