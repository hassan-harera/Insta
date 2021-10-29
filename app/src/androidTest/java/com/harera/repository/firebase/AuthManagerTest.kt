package com.harera.repository.firebase

import com.google.android.gms.tasks.Tasks
import com.google.common.truth.Truth
import com.harera.repository.di.FirebaseModule
import com.harera.repository.di.RepoModule
import com.harera.repository.di.UtilsModule
import com.harera.repository.db.network.abstract_.AuthManager
import com.harera.repository.di.AppModule
import com.harera.repository.di.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.test.KoinTest
import org.koin.test.inject

@ExperimentalCoroutinesApi
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
    fun checkUser() = runBlockingTest(Dispatchers.IO) {
        val firebaseUser = authManager.getCurrentUser()
        Truth.assertThat(firebaseUser?.uid).isEqualTo("UnpL3Oj9ysc2bF8Zlbfz2kMogaY2")
    }

    @Test
    fun loginAnonymously() = runBlockingTest(Dispatchers.IO) {
        val task = authManager.loginAnonymously()
        Tasks.await(task)
        Assert.assertNull(task.exception)
    }


    @Test
    fun signInWithAnonEmail() = runBlockingTest(Dispatchers.IO) {
        val task =
            authManager.signInWithEmailAndRandomPassword("hassan.shaban.harera@gmail.com")
        Tasks.await(task)
        Assert.assertNull(task.exception)
    }

    @Test
    fun signOut() = runBlockingTest(Dispatchers.IO) {
        authManager.signOut()
    }
}