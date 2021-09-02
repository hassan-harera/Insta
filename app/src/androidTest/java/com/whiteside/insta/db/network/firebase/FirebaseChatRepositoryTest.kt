package com.whiteside.insta.db.network.firebase

import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Timestamp
import com.whiteside.insta.db.network.abstract_.ChatRepository
import com.whiteside.insta.modelset.Chat
import com.whiteside.insta.modelset.Message
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class FirebaseChatRepositoryTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var chatRepository: ChatRepository

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun getMessages() {
        val task = chatRepository.getMessages(
            "N2duudxGtoVyTwkqzGMW4Al336H3",
            "NKVxniXACjMDET8YApYjSCaHu6u2",
        )
        task.size.let {
            Assert.assertEquals(8, it)
        }
    }

    @Test
    fun getLastMessage() {
        val task = chatRepository.getLastMessage(
            "NKVxniXACjMDET8YApYjSCaHu6u2",
            "N2duudxGtoVyTwkqzGMW4Al336H3"
        )

        task.let {
            val message = it.last().toObject(com.whiteside.insta.modelget.Message::class.java)!!
            Log.d("getLastMessage", message.time.toString())
            Assert.assertNotNull(message.to)
        }
    }

    @Test
    fun saveMessage() {
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
    fun addOpenChat() {
        val task = chatRepository.addOpenChat(
            Chat(
                firstUid = "NKVxniXACjMDET8YApYjSCaHu6u2",
                secondUid = "N2duudxGtoVyTwkqzGMW4Al336H3",
            )
        )
        Tasks.await(task)
        Assert.assertNotNull(task)
    }

    @Test
    fun getOpenChats() {
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