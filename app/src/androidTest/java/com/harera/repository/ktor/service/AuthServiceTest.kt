package com.harera.repository.ktor.service

import com.harera.remote.service.AuthenticationService
import org.koin.test.KoinTest
import org.koin.test.inject

class AuthServiceTest : KoinTest {

    val authService: AuthenticationService by inject()

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