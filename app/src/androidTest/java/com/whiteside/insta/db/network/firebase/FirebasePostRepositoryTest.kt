package com.whiteside.insta.db.network.firebase

import android.graphics.Bitmap
import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Timestamp
import com.whiteside.insta.db.network.abstract_.AuthManager
import com.whiteside.insta.db.network.abstract_.PostRepository
import com.whiteside.insta.modelset.Comment
import com.whiteside.insta.modelset.Like
import com.whiteside.insta.modelset.Post
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import kotlin.random.Random

@HiltAndroidTest
class FirebasePostRepositoryTest {

    private var postImageUrl: String =
        "https://firebasestorage.googleapis.com/v0/b/insta-simulator.appspot.com/o/users%2FN2duudxGtoVyTwkqzGMW4Al336H3%2FprofilePic?alt=media&token=7694a651-c0cb-4ee4-9152-c3aed36eab02"

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var postRepository: PostRepository

    @Inject
    lateinit var authManager: AuthManager

    @Before
    fun init() {
        hiltRule.inject()
    }

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
            .toObject(com.whiteside.insta.modelget.Post::class.java)!!
    }
}