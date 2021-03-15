package com.whiteside.insta.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Blob
import com.whiteside.insta.BR

class Post : BaseObservable() {
    @get:Bindable
    var postImage: Blob? = null
        set(value) {
            field = value
            notifyChange()
        }

    @get:Bindable
    var time: Timestamp? = Timestamp.now()
        set(value) {
            field = value
            notifyChange()
        }

    @get:Bindable
    var likes: HashMap<String, Timestamp>? = HashMap()
        set(value) {
            field = value
            notifyChange()
        }

    @get:Bindable
    var caption: String? = ""
        set(value) {
            field = value
            notifyChange()
        }

    var Id: String? = ""
    var UID: String? = ""
    var comments: Map<String, Comment>? = HashMap()
    var shares: Map<String, Share>? = null
}