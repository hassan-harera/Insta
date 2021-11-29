package com.harera.remote.service

import com.harera.insta.di.ServiceModule
import com.harera.insta.di.serviceModule
import io.ktor.util.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import org.koin.test.inject

@InternalAPI
class PostServiceImplTest : KoinTest {

    val postService : PostService by inject()

    @Before
    fun setUp() {
        startKoin {
            loadKoinModules(
                arrayListOf(
                    ServiceModule,
                )
            )
        }
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getFeedPosts() {
    }

    @Test
    fun getPost() {
    }

    @Test
    fun addPost() {
    }
}