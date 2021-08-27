package com.whiteside.insta.db.network.firebase

import com.google.android.gms.tasks.Tasks
import com.whiteside.insta.db.network.abstract_.ChatRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class FirebaseChatRepositoryTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var chatRepository: ChatRepository

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun getMessages() {
        val task = chatRepository.getMessages("")
        Tasks.await(task)
        Assert.assertNotNull(task)
    }
}