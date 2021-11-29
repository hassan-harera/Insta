package com.harera.search

import com.harera.base.state.BaseState
import com.harera.model.response.SearchedPost

sealed class SearchState : BaseState() {
    data class PostsSearchResult(val posts : List<SearchedPost>) : SearchState()
}
