package com.harera.repository.firebase

import android.graphics.Bitmap
import com.google.common.truth.Truth.assertThat
import com.google.firebase.Timestamp
import com.harera.repository.di.FirebaseModule
import com.harera.repository.di.RepoModule
import com.harera.repository.di.UtilsModule
import com.harera.repository.di.ViewModel
import com.harera.model.model.Post
import com.harera.repository.db.network.abstract_.AuthManager
import com.harera.repository.db.network.abstract_.PostRepository
import com.harera.repository.di.AppModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.random.Random

@ExperimentalCoroutinesApi
class FirebasePostRepositoryTest : KoinTest {

    private val authManager: AuthManager by inject()
    private val postRepository: PostRepository by inject()

    @Before
    fun setup() {
        loadKoinModules(
            modules = arrayListOf(
                AppModule,
                FirebaseModule,
                ViewModel,
                RepoModule,
                UtilsModule
            )
        )
    }

    private var postImageUrl: String =
        "https://firebasestorage.googleapis.com/v0/b/insta-simulator.appspot.com/o/users%2FN2duudxGtoVyTwkqzGMW4Al336H3%2FprofilePic?alt=media&token=7694a651-c0cb-4ee4-9152-c3aed36eab02"

    @Test
    fun addPost() = runBlockingTest {
        for (i in 1..10) {
            var caption = "Caption"
            for (k in 1..10)
                caption = caption.plus(Char(Random.nextInt(24) + 97))

            val task = postRepository.addPost(
                Post(
                    caption = caption,
                    uid = "UnpL3Oj9ysc2bF8Zlbfz2kMogaY2",
                    postId = Random.nextInt(100).toString(),
                    postImageUrl =
                    "https://firebasestorage.googleapis.com/v0/b/insta-simulator.appspot.com/" +
                            "o/users%2FN2duudxGtoVyTwkqzGMW4Al336H3%2Fposts%2F1234.png?alt=media&token=6e6a625c-83bb-4d9d-9e50-908cffd56f86",
                    time = Timestamp.now()
                )
            )
            Assert.assertNotNull(task)
        }
    }

    @Test
    fun uploadPostImage() = runBlockingTest(Dispatchers.IO) {
        val url = postRepository.uploadPostImage(
            Bitmap.createBitmap(512, 512, Bitmap.Config.ALPHA_8),
            "11",
            authManager.getCurrentUser()!!.uid
        )
        Assert.assertNotNull(url)
    }

    @Test
    fun getNewPostId() = runBlockingTest(Dispatchers.IO) {
        val task = postRepository.getNewPostId(authManager.getCurrentUser()!!.uid)
        Assert.assertNotNull(task)
    }

    @Test
    fun getFeedPosts() = runBlockingTest(Dispatchers.IO) {
        val posts = postRepository.getFeedPosts(
            arrayListOf(
                "NKVxniXACjMDET8YApYjSCaHu6u2",
            ),
            10
        )
        assertThat(posts).isEqualTo(10)
    }

    @Test
    fun getUserPosts() = runBlockingTest {
        val posts = postRepository.getUserPosts(
                "UnpL3Oj9ysc2bF8Zlbfz2kMogaY2",
            10
        ).onSuccess {
            assertThat(it.size).isEqualTo(10)
        }
    }

    @Test
    fun checkSearchResults() = runBlockingTest(Dispatchers.IO) {
        val posts = postRepository.searchPosts("hassan")
        assertThat(posts).isEqualTo(10)
    }

//    @Test
//    fun getLikes() = runBlockingTest(Dispatchers.IO) {
//        val task = postRepository.getPostLikes("11")
//        Tasks.await(task)
//        Assert.assertNull(task.exception)
//        Assert.assertEquals(10, task.result.size())
//    }

//    @Test
//    fun getComments() = runBlockingTest(Dispatchers.IO) {
//        val task = postRepository.getPostComments("11")
//        Tasks.await(task)
//        Assert.assertNull(task.exception)
//        Assert.assertEquals(10, task.result.size())
//    }
//
//    @Test
//    fun addComment() = runBlockingTest(Dispatchers.IO) {
//        for (i in 1..10) {
//            val task = postRepository.addComment(
//                CommentToPost(
//                    postId = "11",
//                    uid = authManager.getCurrentUser()!!.uid,
//                    time = Timestamp.now(),
//                    comment = "hassan",
//                )
//            )
//            Tasks.await(task)
//            Assert.assertNull(task.exception)
////        Assert.assertEquals(10, task.result.size())
//        }
//    }
//
//    @Test
//    fun addLike() = runBlockingTest(Dispatchers.IO) {
//        for (i in 1..10) {
//            val task = postRepository.addLike(
//                LikePost(
//                    postId = "11",
//                    time = Timestamp.now(),
//                    uid = authManager.getCurrentUser()!!.uid,
//                )
//            )
//            Tasks.await(task)
//            Assert.assertNull(task.exception)
//        }
//    }
//
//    @Test
//    fun getPostFetched() = runBlockingTest(Dispatchers.IO) {
//        val task = postRepository
//            .getPostFetched("EeKXs3aMhUygITd7sVWv")
//
//        Tasks.await(task)
//            .toObject(com.harera.model.modelget.PostFetched::class.java)!!
//    }
}