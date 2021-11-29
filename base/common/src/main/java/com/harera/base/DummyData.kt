package com.harera.base

import com.harera.model.model.*
import com.harera.model.response.PostResponse
import org.joda.time.DateTime

object DummyDate {
    val POST: Post = Post(
        time = "",
        caption = "Caption",
        username = "uid",
        postImageUrl = "",
        postId = 1223,
        type = 1,
    )

    val MESSAGE: Message = Message(
        time = DateTime.now(),
        sender = "senderUID",
        receiver = "receiverUID",
        message = "handledMessage",
    )

    val USER = User(
        bio = "Profile Bio",
        email = "hassanstar201118@gmail.com",
        name = "Profile Name",
        username = "Uid",
        password = "fkjsdhfkjahsdfkas",
        uid = 123,
        phoneNumber = "fjlskfj",
        userImageUrl =
        "https://firebasestorage.googleapis.com/v0/b/insta-simulator.appspot.com/o/users%2FDedESk6CTHa0B94FJyRLoBMX9IT2%2Fimage%20(55).jpg?alt=media&token=1c373fd7-6fa9-4ef2-96b0-e1647bd3640e"
    )

    val OPEN_CHAT: OpenChat = OpenChat(
        profileName = "Profile Name",
        time = org.joda.time.DateTime(),
        lastMessage = "Hello guys",
        username = "Uid",
        profileImageUrl =
        "https://firebasestorage.googleapis.com/v0/b/insta-simulator.appspot.com/o/users%2FDedESk6CTHa0B94FJyRLoBMX9IT2%2Fimage%20(55).jpg?alt=media&token=1c373fd7-6fa9-4ef2-96b0-e1647bd3640e"
    )

    val POST_DETAILS: PostResponse = PostResponse(
        post = POST,
        user = USER,
        comments = emptyList(),
        likes = emptyList()
    )
}