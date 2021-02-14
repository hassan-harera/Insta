package Model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Blob

class Post(
        var caption: String? = "",
        var Id: String? = "",
        var uId: String? = "",
        var time: Timestamp? = Timestamp.now(),
        var liked: Boolean? = false,
        var postImage: Blob? = null,
        var likes: HashMap<String, Timestamp>? = HashMap(),
        var comments: Map<String, Comment>? = HashMap(),
        var shares: Map<String, Share>? = null) : Comparable<Post> {


    fun addLike(UID: String) {
        likes!!.remove(UID)
        likes!![UID] = Timestamp.now()
    }

    fun removeLike(UID: String) {
        likes!!.remove(UID)
    }

    override fun compareTo(o: Post): Int {
        return o.time!!.compareTo(time!!)
    }
}