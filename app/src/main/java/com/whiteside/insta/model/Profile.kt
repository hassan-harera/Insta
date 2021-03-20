package com.whiteside.insta.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Blob
import com.whiteside.insta.BR

class Profile : BaseObservable() {

    @get:Bindable
    var profilePic: Blob? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.profilePic)
        }

    @get:Bindable
    var uid: String? = null
        set(value) {
            field = value
//            notifyPropertyChanged(BR.uid)
        }

    @get:Bindable
    var name: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.name)
        }

    @get:Bindable
    var email: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.email)
        }

    @get:Bindable
    var bio: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.bio)
        }

    var friendRequests: HashMap<String, Timestamp>? = null
    var friends: ArrayList<String>? = null

    fun addFriendRequest(UID: String) {
        friendRequests!![UID] = Timestamp.now()
    }

    fun removeFriendRequest(UID: String) {
        friendRequests!!.remove(UID)
    }

    fun addFriend(UID: String) {
        friends!!.add(UID)
    }
}