package com.harera.model.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
@Entity
class Chat {
    @PrimaryKey
    @NonNull
    var id = 0
    lateinit var secondUid: String
    lateinit var firstUid: String

    constructor(firstUid: String, secondUid: String) {
        this.firstUid = firstUid
        this.secondUid = secondUid
    }

    constructor() {}
}