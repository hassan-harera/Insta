package com.harera.model.modelget

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
class Chat {
    lateinit var firstUid: String
    lateinit var secondUid: String
}