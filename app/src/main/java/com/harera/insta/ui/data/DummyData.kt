package com.harera.insta.ui.data

import com.google.firebase.Timestamp
import com.harera.model.modelget.*

object DummyDate {
    val POST : Post =  Post().apply {
        time = Timestamp.now()
        this.caption = "Caption"
        this.uid = "uid"
        this.profileName = "Hassan"
        this.likesNumber = "53"
        this.commentsNumber = "66"
        this.profileImageUrl =
            "https://firebasestorage.googleapis.com/v0/b/insta-simulator.appspot.com/o/users%2FDedESk6CTHa0B94FJyRLoBMX9IT2%2Fimage%20(55).jpg?alt=media&token=1c373fd7-6fa9-4ef2-96b0-e1647bd3640e"
        this.postImageUrl =
            "https://firebasestorage.googleapis.com/v0/b/insta-simulator.appspot.com/o/users%2FDedESk6CTHa0B94FJyRLoBMX9IT2%2Fimage%20(55).jpg?alt=media&token=1c373fd7-6fa9-4ef2-96b0-e1647bd3640e"
    }

    val COMMENT : Comment = Comment().apply {
        comment = "Comment"
        commentId = "55"
        time = Timestamp.now()
        postId = "83"
        uid = "11"
    }

    val MESSAGE : Message = Message().apply {
        message = "Message"
        time = Timestamp.now()
        from = "123"
        to = "123"
    }

    val CHAT : Chat = Chat().apply {
        firstUid = ""
        secondUid = ""
    }

    val PROFILE : Profile = Profile().apply {
        bio = "Profile Bio"
        email = "hassanstar201118@gmail.com"
        name = "Profile Name"
        uid = "Uid"
        profileImageUrl =
            "https://firebasestorage.googleapis.com/v0/b/insta-simulator.appspot.com/o/users%2FDedESk6CTHa0B94FJyRLoBMX9IT2%2Fimage%20(55).jpg?alt=media&token=1c373fd7-6fa9-4ef2-96b0-e1647bd3640e"
    }

    val OPEN_CHAT : OpenChat = OpenChat().apply {
        profileName = "Profile Name"
        time = Timestamp.now()
        lastMessage = "Hello guys"
        uid = "Uid"
        profileImageUrl =
            "https://firebasestorage.googleapis.com/v0/b/insta-simulator.appspot.com/o/users%2FDedESk6CTHa0B94FJyRLoBMX9IT2%2Fimage%20(55).jpg?alt=media&token=1c373fd7-6fa9-4ef2-96b0-e1647bd3640e"
    }
}