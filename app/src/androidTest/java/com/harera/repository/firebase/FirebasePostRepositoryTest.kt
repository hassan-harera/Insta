package com.harera.repository.firebase

import android.graphics.Bitmap
import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Timestamp
import com.harera.insta.di.FirebaseModule
import com.harera.insta.di.RepoModule
import com.harera.insta.di.UtilsModule
import com.harera.insta.di.ViewModel
import com.harera.model.modelset.Comment
import com.harera.model.modelset.Like
import com.harera.model.modelset.Post
import com.harera.repository.db.network.abstract_.AuthManager
import com.harera.repository.db.network.abstract_.PostRepository
import com.harera.repository.di.AppModule
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.random.Random

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
    fun addPost() {
        for (i in 1..10) {
            var caption = "Caption"
            for (k in 1..10)
                caption = caption.plus(Char(Random.nextInt(24) + 97))

            val task = postRepository.addPost(
                Post(
                    caption = caption,
                    uid = "N2duudxGtoVyTwkqzGMW4Al336H3",
                    postId = Random.nextInt(100).toString(),
                    postImageUrl =
                    "https://firebasestorage.googleapis.com/v0/b/insta-simulator.appspot.com/" +
                            "o/users%2FN2duudxGtoVyTwkqzGMW4Al336H3%2Fposts%2F1234.png?alt=media&token=6e6a625c-83bb-4d9d-9e50-908cffd56f86",
                    time = Timestamp.now()
                )
            )
            Tasks.await(task)
            Assert.assertNull(task.exception)
        }
    }

    @Test
    fun uploadPostImage() {
        val task = postRepository.uploadPostImage(
            Bitmap.createBitmap(512, 512, Bitmap.Config.ALPHA_8),
            "11",
            authManager.getCurrentUser()!!.uid
        )
        Tasks.await(task).let {
            Tasks.await(it.storage.downloadUrl)
                .toString().let {
                    Log.d("uploadPostImage", it)
                }
        }
        Assert.assertNull(task.exception)
    }

    @Test
    fun getNewPostId() {
        val task = postRepository.getNewPostId(authManager.getCurrentUser()!!.uid)
        Assert.assertNotNull(task)
    }

    @Test
    fun getFeedPosts() {
        val task = postRepository.getFeedPosts(
            arrayListOf(
                "NKVxniXACjMDET8YApYjSCaHu6u2",
            ),
            10
        )

        val size = Tasks.await(task).size()
        Assert.assertEquals(10, size)
    }

    @Test
    fun checkSearchResults() {
        val task = postRepository.searchPosts("hassan")
        Tasks.await(task)
        Assert.assertNull(task.exception)
        Assert.assertEquals(10, task.result.size())
    }

    @Test
    fun getLikes() {
        val task = postRepository.getPostLikes("11")
        Tasks.await(task)
        Assert.assertNull(task.exception)
        Assert.assertEquals(10, task.result.size())
    }

    @Test
    fun getComments() {
        val task = postRepository.getPostComments("11")
        Tasks.await(task)
        Assert.assertNull(task.exception)
        Assert.assertEquals(10, task.result.size())
    }

    @Test
    fun addComment() {
        for (i in 1..10) {
            val task = postRepository.addComment(
                Comment(
                    postId = "11",
                    uid = authManager.getCurrentUser()!!.uid,
                    time = Timestamp.now(),
                    comment = "hassan",
                )
            )
            Tasks.await(task)
            Assert.assertNull(task.exception)
//        Assert.assertEquals(10, task.result.size())
        }
    }

    @Test
    fun addLike() {
        for (i in 1..10) {
            val task = postRepository.addLike(
                Like(
                    postId = "11",
                    time = Timestamp.now(),
                    uid = authManager.getCurrentUser()!!.uid,
                )
            )
            Tasks.await(task)
            Assert.assertNull(task.exception)
        }
    }

    @Test
    fun getPost() {
        val task = postRepository
            .getPost("EeKXs3aMhUygITd7sVWv")

        Tasks.await(task)
            .toObject(com.harera.model.modelget.Post::class.java)!!
    }
}