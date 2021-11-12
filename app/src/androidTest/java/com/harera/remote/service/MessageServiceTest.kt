package com.harera.remote.service

import com.google.common.truth.Truth
import com.harera.insta.di.ServiceModule
import com.harera.remote.request.MessageInsertRequest
import io.ktor.util.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import org.koin.test.inject

@ExperimentalCoroutinesApi
@InternalAPI
class MessageServiceTest : KoinTest {

    private val messageService: MessageService by inject()

    @Before
    fun setUp() {}

    @Test
    fun insertMessage() = runBlockingTest {
        runBlocking {
            messageService.insertMessage(
                token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJodHRwOi8vMC4wLjAuMDo4MDgwL2hlbGxvIiwiaXNzIjoiaHR0cDovLzAuMC4wLjA6ODA4MC8iLCJleHAiOjE2Mzg2MDA1NjQsInVzZXJuYW1lIjoiMSJ9.PV1isIjteTstHmkectYQSikXVeYF4UNny9TZuO2Z4gM",
                MessageInsertRequest(
                    receiver = "1",
                    message = "Hi Hassan"
                )
            )
        }.let {
            Truth.assertThat(it).contains("success")
        }
    }

    @Test
    fun deleteMessage() = runBlockingTest {
        runBlocking {
            messageService.deleteMessage(
                token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJodHRwOi8vMC4wLjAuMDo4MDgwL2hlbGxvIiwiaXNzIjoiaHR0cDovLzAuMC4wLjA6ODA4MC8iLCJleHAiOjE2Mzg2MDA1NjQsInVzZXJuYW1lIjoiMSJ9.PV1isIjteTstHmkectYQSikXVeYF4UNny9TZuO2Z4gM",
                19
            )
        }.let {
            Truth.assertThat(it).contains("success")
        }
    }

    @Test
    fun getMessage()  = runBlockingTest {
        runBlocking {
            messageService.getMessage(
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJodHRwOi8vMC4wLjAuMDo4MDgwL2hlbGxvIiwiaXNzIjoiaHR0cDovLzAuMC4wLjA6ODA4MC8iLCJleHAiOjE2Mzg2MDA1NjQsInVzZXJuYW1lIjoiMSJ9.PV1isIjteTstHmkectYQSikXVeYF4UNny9TZuO2Z4gM",
                20
            )
        }.let {
            Truth.assertThat(it.messageId).isEqualTo(18)
        }
    }

    @Test
    fun getMessages()  = runBlockingTest {
        runBlocking {
            messageService.getMessages(
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJodHRwOi8vMC4wLjAuMDo4MDgwL2hlbGxvIiwiaXNzIjoiaHR0cDovLzAuMC4wLjA6ODA4MC8iLCJleHAiOjE2Mzg2MDA1NjQsInVzZXJuYW1lIjoiMSJ9.PV1isIjteTstHmkectYQSikXVeYF4UNny9TZuO2Z4gM",
                "5"
            )
        }.let {
            Truth.assertThat(it.size).isEqualTo(15)
        }
    }

    @Test
    fun getChats()  = runBlockingTest {
        runBlocking {
            messageService.getChats(
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJodHRwOi8vMC4wLjAuMDo4MDgwL2hlbGxvIiwiaXNzIjoiaHR0cDovLzAuMC4wLjA6ODA4MC8iLCJleHAiOjE2Mzg2MDA1NjQsInVzZXJuYW1lIjoiMSJ9.PV1isIjteTstHmkectYQSikXVeYF4UNny9TZuO2Z4gM",
            )
        }.let {
            Truth.assertThat(it.size).isEqualTo(3)
        }
    }
}