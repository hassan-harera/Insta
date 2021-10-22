package com.harera.repository.firebase

import com.google.android.gms.tasks.Tasks
import com.harera.insta.di.FirebaseModule
import com.harera.insta.di.RepoModule
import com.harera.insta.di.UtilsModule
import com.harera.insta.di.ViewModel
import com.harera.repository.db.network.abstract_.AuthManager
import com.harera.repository.di.AppModule
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.test.KoinTest
import org.koin.test.inject

class AuthManagerTest : KoinTest {

    private val authManager: AuthManager by inject()

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