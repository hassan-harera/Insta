package Model.Notifications

class FriendRequestNotification : Notification() {
    var uID: String? = null

    init {
        type = 2
    }
}