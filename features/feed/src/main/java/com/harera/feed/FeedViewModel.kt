package com.harera.feed

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.harera.base.base.BaseViewModel
import com.harera.base.datastore.LocalStore
import com.harera.base.state.BaseState
import com.harera.base.state.FeedState
import com.harera.base.state.PostState
import com.harera.repository.PostRepository
import com.harera.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class FeedViewModel constructor(
    private val postRepository: PostRepository,
    private val profileRepository: ProfileRepository,
    userSharedPreferences: LocalStore,
) : BaseViewModel<FeedState>(userSharedPreferences) {

    var intent = Channel<FeedIntent>()
    private var page by mutableStateOf(1)

    init {
        processIntent()
    }

    private fun processIntent() {
        viewModelScope.launch(Dispatchers.IO) {
            intent.consumeAsFlow().collect { intent ->
                when (intent) {
                    is FeedIntent.FetchPosts -> {
                        getFeedPosts()
                    }
                    is FeedIntent.LoadMorePosts -> {
                        page++
                        loadMorePosts()
                    }
                }
            }
        }
    }

    private suspend fun loadMorePosts() {
        state = BaseState.Loading()
        postRepository.getFeedPosts(token!!, page)
            .onFailure {
                state = PostState.Error(it.message)
                handleFailure(it)
            }.onSuccess { posts ->
                state = FeedState.MorePosts(posts)
            }
    }

    private suspend fun getFeedPosts() {
        state = BaseState.Loading()
        postRepository.getFeedPosts(token!!, page)
            .onFailure {
                state = PostState.Error(it.message)
                handleFailure(it)
            }.onSuccess { posts ->
                state = FeedState.Posts(posts)
            }
    }
}
