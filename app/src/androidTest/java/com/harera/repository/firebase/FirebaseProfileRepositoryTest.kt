package com.harera.repository.firebase

import android.graphics.Bitmap
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.harera.insta.di.FirebaseModule
import com.harera.insta.di.RepoModule
import com.harera.insta.di.UtilsModule
import com.harera.insta.di.ViewModel
import com.harera.model.modelget.Post
import com.harera.model.modelset.FollowRelation
import com.harera.model.modelset.FollowRequest
import com.harera.model.modelset.Profile
import com.harera.repository.common.Constansts.FOLLOWERS
import com.harera.repository.common.Constansts.POSTS
import com.harera.repository.db.network.abstract_.AuthManager
import com.harera.repository.db.network.abstract_.ProfileRepository
import com.harera.repository.di.AppModule
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.test.KoinTest
import org.koin.test.inject

class FirebaseProfileRepositoryTest : KoinTest {

    private val authManager: AuthManager by inject()
    private val profileRepository: ProfileRepository by inject()

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

    private var imageUrl: String =
        "https://firebasestorage.googleapis.com/v0/b/insta-simulator.appspot.com/o/users%2FN2duudxGtoVyTwkqzGMW4Al336H3%2FprofilePic?alt=media&token=7694a651-c0cb-4ee4-9152-c3aed36eab02"

    @Test
    fun addProfile() {
        val task = profileRepository.addProfile(
            Profile(
                uid = "UnpL3Oj9ysc2bF8Zlbfz2kMogaY2",
                bio = "Hassan",
                email = "hassan.shaban.harera@gmail.com",
                name = "Hassan",
                profileImageUrl = imageUrl
            )
        )
        Tasks.await(task)
        Assert.assertNull(task.exception)
    }

    @Test
    fun uploadProfileImage() {
        val imageBitmap = Bitmap.createBitmap(512, 512, Bitmap.Config.ALPHA_8)
        val task =
            profileRepository.updateProfilePicture(imageBitmap, authManager.getCurrentUser()!!.uid)
        imageUrl = task.result.uploadSessionUri.toString()
        Tasks.await(task)
        Assert.assertNull(task.exception)
    }

    @Test
    fun updateProfileImageUrl() {
        val task =
            profileRepository.updateProfileImageUrl(authManager.getCurrentUser()!!.uid, imageUrl)
        Tasks.await(task)
        Assert.assertNull(task.exception)
    }


    @Test
    fun getFollowers() {
        val task = profileRepository.getFollowers(authManager.getCurrentUser()!!.uid)
        Tasks.await(task)
        Assert.assertNull(task.exception)
        Assert.assertEquals(1, task.result.size())
    }

    @Test
    fun checkProfileImageUploadUrlResult() {
        val imageBitmap = Bitmap.createBitmap(512, 512, Bitmap.Config.ALPHA_8)
        val task =
            profileRepository.updateProfilePicture(imageBitmap, authManager.getCurrentUser()!!.uid)
        val imageUrl = Tasks.await(task).storage.downloadUrl.toString()
        Assert.assertEquals(
            imageUrl,
            "https://firebasestorage.googleapis.com/v0/b/insta-simulator.appspot.com/o/users%2FN2duudxGtoVyTwkqzGMW4Al336H3%2FprofilePic?alt=media&token=224769b8-b8ae-468d-a633-200b93657d50"
        )
    }

    @Test
    fun addFollowing() {
        val task = profileRepository.addFollowing(
            FollowRelation(
                "UnpL3Oj9ysc2bF8Zlbfz2kMogaY2",
                "N2duudxGtoVyTwkqzGMW4Al336H3",
                Timestamp.now(),
            )
        )
        Tasks.await(task)
        Assert.assertNull(task.exception)
    }

    @Test
    fun addFollowingRequest() {
        val task = profileRepository.addFollowRequest(
            FollowRequest(
                authManager.getCurrentUser()!!.uid,
                "n5HrbJmWuYeAXwlxiNyQuKqjTM43",
                Timestamp.now(),
            )
        )
        Tasks.await(task)
        Assert.assertNotNull(task)
    }

    @Test
    fun addFollower() {
        val task = profileRepository.addFollower(
            FollowRelation(
                authManager.getCurrentUser()!!.uid,
                "NKVxniXACjMDET8YApYjSCaHu6u2",
                Timestamp.now(),
            )
        )
        Tasks.await(task)
        Assert.assertNotNull(task)
    }

    @Test
    fun getFollowRequests() {
        val task = profileRepository.getFollowRequests(authManager.getCurrentUser()!!.uid)
        Tasks.await(task)
        Assert.assertEquals(task.result.size(), 1)
    }

    @Test
    fun getFollowings() {
        val task = profileRepository.getFollowings(authManager.getCurrentUser()!!.uid)
        Tasks.await(task)
        Assert.assertEquals(task.result.size(), 1)
    }

    @Test
    fun getMutualFollowings() {
        val getFollowings = profileRepository
            .getFollowings("n5HrbJmWuYeAXwlxiNyQuKqjTM43")

        val followingsList = Tasks.await(getFollowings).documents.map {
            it.toObject(com.harera.model.modelget.FollowRelation::class.java)!!.followingUid
        }

        val task2 = profileRepository.getMutualFollowings(
            authManager.getCurrentUser()!!.uid,
            followingsList
        )
        Tasks.await(task2)
        task2.result.size().let {
            Assert.assertEquals(it, 1)
        }
        task2.result.let {
            it.toObjects(com.harera.model.modelget.FollowRelation::class.java)
        }
    }

    @Test
    fun checkWhereInQuery() {
        FirebaseFirestore.getInstance().collection(FOLLOWERS).whereIn(
            FollowRelation::followerUid.name,
            arrayListOf("n5HrbJmWuYeAXwlxiNyQuKqjTM43")
        ).whereEqualTo(FollowRelation::followingUid.name, "NKVxniXACjMDET8YApYjSCaHu6u2")
            .get().let {
                Tasks.await(it).let {
                    Assert.assertEquals(it.documents.size, 0)
                }
            }
    }

    @Test
    fun getProfile() {
        profileRepository.getProfile("NKVxniXACjMDET8YApYjSCaHu6u2").let {
            Tasks.await(it).let {
                val profile = it.toObject(com.harera.model.modelget.Profile::class.java)!!
                Assert.assertNotNull(profile)
            }
        }
    }


    @Test
    fun updatePostsImageUrl() {
        val task = FirebaseFirestore
            .getInstance()
            .collection(POSTS)
            .get()

        Tasks.await(task)
            .documents
            .forEach {
                FirebaseFirestore
                    .getInstance()
                    .collection(POSTS)
                    .document(it.id)
                    .update(
                        Post::postImageUrl.name,
                        "https://firebasestorage.googleapis.com/v0/b/insta-simulator.appspot.com/o/users%2FN2duudxGtoVyTwkqzGMW4Al336H3%2Fposts%2F1234.png?alt=media&token=6e6a625c-83bb-4d9d-9e50-908cffd56f86"
                    )
                    .let {
                        Tasks.await(it)
                    }
            }
    }
}