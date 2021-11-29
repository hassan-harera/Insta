package com.harera.remote.repository

import com.harera.model.response.SearchedPost
import com.harera.remote.service.SearchService
import com.harera.repository.SearchRepository

class SearchRepositoryImpl(
   private val searchService : SearchService
) : SearchRepository {

    override suspend fun searchPosts(text: String): Result<List<SearchedPost>> =  kotlin.runCatching{
        searchService.searchPosts(text = text)
    }
}