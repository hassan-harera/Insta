package com.harera.remote.service

import com.google.common.truth.Truth
import io.ktor.util.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

@ExperimentalCoroutinesApi
@InternalAPI
class UserServiceTest : KoinTest {

    private val profileService: ProfileService by inject()

    @Before
    fun setUp() {}

    @Test
    fun getUser()  = runBlockingTest {
        runBlocking {
            profileService.getUser(
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJodHRwOi8vMC4wLjAuMDo4MDgwL2hlbGxvIiwiaXNzIjoiaHR0cDovLzAuMC4wLjA6ODA4MC8iLCJleHAiOjE2Mzg2MDA1NjQsInVzZXJuYW1lIjoiMSJ9.PV1isIjteTstHmkectYQSikXVeYF4UNny9TZuO2Z4gM",
            "1"
            )
        }.let {
            Truth.assertThat(it).isNotNull()
            Truth.assertThat(it.username).isEqualTo("1")
        }
    }
}