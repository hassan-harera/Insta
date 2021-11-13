package com.harera.repository.ktor.service

import com.harera.insta.di.ServiceModule
import com.harera.remote.service.PostService
import io.ktor.util.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import org.koin.test.inject

class PostServiceImplTest : KoinTest {

    val postService: PostService by inject()
    val token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJodHRwOi8vMC4wLjAuMDo4MDgwL2hlbGxvIiwiaXNzIjoiaHR0cDovLzAuMC4wLjA6ODA4MC8iLCJleHAiOjE2MzkwMjQ5NDksInVzZXJuYW1lIjoiNSJ9._Ur3iq5gZfw5w3QOfg0_vivU3RHBFWZbKqpY1yAPJ5I"

    @InternalAPI
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

    @ExperimentalCoroutinesApi
    @Test
    fun addPost() = runBlockingTest {
//        val bmp: Bitmap? = null
//        val stream = ByteArrayOutputStream()
//        bmp?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
//        val byteArray: ByteArray = stream.toByteArray()
//
//        val fileOuputStream = FileOutputStream("C:\\testing2.txt")
//        fileOuputStream.write(bFile)
//        fileOuputStream.close()
//
//        postService.addPost(
//            token,
//            caption = "",
//            byteArray.
//        )
    }
}