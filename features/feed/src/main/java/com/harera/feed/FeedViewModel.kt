package com.harera.feed

import androidx.lifecycle.viewModelScope
import com.harera.base.base.BaseViewModel
import com.harera.base.datastore.LocalStore
import com.harera.base.state.FeedState
import com.harera.base.state.PostState
import com.harera.base.state.State
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
                }
            }
        }
    }

    private suspend fun getFeedPosts() {
        state = State.Loading()
        postRepository.getFeedPosts(token!!)
            .onFailure {
                state = PostState.Error(it.message)
                handleFailure(it)
            }.onSuccess { posts ->
                state = FeedState.Posts(posts)
            }
    }
}
