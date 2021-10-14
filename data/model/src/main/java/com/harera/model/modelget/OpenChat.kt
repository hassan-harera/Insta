package com.harera.model.modelget

import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
class OpenChat {
    lateinit var uid: String
    lateinit var profileImageUrl: String
    lateinit var lastMessage: String
    lateinit var time: Timestamp
    lateinit var profileName: String
}
