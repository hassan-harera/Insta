package com.harera.repository.firebase

import com.google.android.gms.tasks.Tasks
import com.harera.repository.db.network.abstract_.AuthManager
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class AuthManagerTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var authManager: AuthManager

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun checkUser() {
        val firebaseUser = authManager.getCurrentUser()
        Assert.assertNotNull(firebaseUser)
    }

    @Test
    fun loginAnonymously() {
        val task = authManager.loginAnonymously()
        Tasks.await(task)
        Assert.assertNull(task.exception)
    }

    @Test
    fun signInWithAnonEmail() {
        val task = authManager.signInWithEmailAndRandomPassword("hassan.shaban.harera@gmail.com")
        Tasks.await(task)
        Assert.assertNull(task.exception)
    }

    @Test
    fun signOut() {
        val task = authManager.signOut()
    }
}