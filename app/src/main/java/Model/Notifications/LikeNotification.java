package Model.Notifications;

public class LikeNotification extends Notification {

    private String postID;
    private int likeNumbers;

    public LikeNotification() {
        super.setType(1);
    }

    public int getLikeNumbers() {
        return likeNumbers;

    }

    public void setLikeNumbers(int likeNumbers) {
        this.likeNumbers = likeNumbers;
    }


    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }
}
