package com.harera.feed

import com.harera.repository.db.network.firebase.FakePostRepository
import com.harera.repository.db.network.firebase.FakeProfileRepository
import org.junit.Before

class FeedViewModelTest {

    private lateinit var feedViewModel: FeedViewModel

    @Before
    fun setup() {
        feedViewModel = FeedViewModel(
            postRepository = FakePostRepository(),
            profileRepository = FakeProfileRepository(),
            authManager = FakeAuthManager()
        )
    }

}