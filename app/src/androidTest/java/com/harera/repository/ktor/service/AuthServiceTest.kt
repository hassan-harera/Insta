package com.harera.repository.ktor.service

import com.harera.insta.di.*
import com.harera.remote.request.CreateUserByEmailRequest
import com.harera.remote.service.AuthService
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.test.KoinTest
import org.koin.test.inject

class AuthServiceTest : KoinTest {

    val authService: AuthService by inject()

//    @Before
//    fun setup() {
//        loadKoinModules(
//            arrayListOf(
//                AppModule,
//                RepoModule,
//                UtilsModule,
//                ServiceModule,
//                FirebaseModule,
//                dbModule,
//            )
//        )
//    }

//    @Test
//    fun `empty email, return error`() = runBlockingTest {
//        authService.signupWithEmail(
//            CreateUserByEmailRequest(
//                email = "",
//                name = "hassan",
//                password = "harera"
//            )
//        )
//    }
}