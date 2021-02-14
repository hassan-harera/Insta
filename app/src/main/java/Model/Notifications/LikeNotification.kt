package Model.Notifications

class LikeNotification : Notification() {
    var postID: String? = null
    var likeNumbers = 0
    var uID: String? = null

    init {
        type = 1
    }
}