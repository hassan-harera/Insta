package com.harera.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harera.base.base.BaseViewModel
import com.harera.base.datastore.LocalStore
import com.harera.repository.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    userLocalStore: LocalStore,
    private val searchRepository : SearchRepository
) : BaseViewModel<SearchState>(userLocalStore) {

    val intent = Channel<SearchIntent>(Channel.UNLIMITED)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            triggerIntent()
        }
    }

    private suspend fun triggerIntent() {
        intent.consumeAsFlow().collect {
            when(it) {
                is SearchIntent.Search -> {
                    searchPosts(it.data)
                }
            }
        }
    }

    private suspend fun searchPosts(data: String) {
        searchRepository
            .searchPosts(data)
            .onSuccess {
                state = SearchState.PostsSearchResult(it)
            }
            .onFailure {
                handleFailure(it)
            }
    }
}