package com.harera.repository.firebase

import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Timestamp
import com.harera.repository.di.FirebaseModule
import com.harera.repository.di.RepoModule
import com.harera.repository.di.UtilsModule
import com.harera.repository.di.ViewModel
import com.harera.model.model.Chat
import com.harera.model.model.Message
import com.harera.repository.db.network.abstract_.AuthManager
import com.harera.repository.db.network.abstract_.ChatRepository
import com.harera.repository.di.AppModule
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
class FirebaseChatRepositoryTest : KoinTest {

    private val authManager: AuthManager by inject()
    private val chatRepository: ChatRepository by inject()

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
    fun getMessages() = runBlockingTest(Dispatchers.IO) {
        val task = chatRepository.getMessages(
            "N2duudxGtoVyTwkqzGMW4Al336H3",
            "NKVxniXACjMDET8YApYjSCaHu6u2",
        )
        task.size.let {
            Assert.assertEquals(8, it)
        }
    }

    @Test
    fun getLastMessage() = runBlockingTest(Dispatchers.IO) {
        val task = chatRepository.getLastMessage(
            "NKVxniXACjMDET8YApYjSCaHu6u2",
            "N2duudxGtoVyTwkqzGMW4Al336H3"
        )

        task.let {
            val message = it.last().toObject(Message::class.java)!!
            Log.d("getLastMessage", message.time.toString())
            Assert.assertNotNull(message.to)
        }
    }

    @Test
    fun saveMessage() = runBlockingTest(Dispatchers.IO) {
        for (i in 1..8) {
            val task = chatRepository.saveMessage(
                message = Message(
                    message = "message",
                    to = "NKVxniXACjMDET8YApYjSCaHu6u2",
                    from = "N2duudxGtoVyTwkqzGMW4Al336H3",
                    time = Timestamp.now()
                )
            )
            Tasks.await(task)
            Assert.assertNotNull(task)
        }
    }

    @Test
    fun addOpenChat() = runBlockingTest(Dispatchers.IO) {
        val task = chatRepository.addOpenChat(
            Chat().apply {
                firstUid = "NKVxniXACjMDET8YApYjSCaHu6u2"
                secondUid = "N2duudxGtoVyTwkqzGMW4Al336H3"
            }
        )
        Tasks.await(task)
        Assert.assertNotNull(task)
    }

    @Test
    fun getOpenChats() = runBlockingTest(Dispatchers.IO) {
        val task = chatRepository.getOpenChats(
            uid = "N2duudxGtoVyTwkqzGMW4Al336H3",
        )

        Tasks.await(task).let {
            it.children.map {
                it.getValue(String::class.java)!!
            }.let {
                Assert.assertEquals(it.size, 1)
            }
        }
    }
}