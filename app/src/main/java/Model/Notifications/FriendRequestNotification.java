package Model.Notifications;

public class FriendRequestNotification extends Notification {

    private String UID;


    public FriendRequestNotification() {
        super.setType(2);
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}
