package Model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Blob

class Profile {
    var name: String? = null
    var email: String? = null
    var bio: String? = null
    var profilePic: Blob? = null
    private var friendRequests: MutableMap<String, Timestamp>? = null
    private var friends: MutableList<String>? = null
    fun getFriends(): List<String>? {
        return friends
    }

    fun setFriends(friends: MutableList<String>?) {
        this.friends = friends
    }

    fun getFriendRequests(): Map<String, Timestamp>? {
        return friendRequests
    }

    fun setFriendRequests(friendRequests: MutableMap<String, Timestamp>?) {
        this.friendRequests = friendRequests
    }

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