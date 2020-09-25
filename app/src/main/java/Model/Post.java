package Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Blob;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class Post implements Comparable<Post> {

    private String caption, ID, UID;
    private Timestamp time;
    private Boolean isLiked;
    private Blob postImage;
    private Map<String, String> likes;
    private Map<String, Comment> comments;
    private Map<String, Share> shares;


    public Map<String, Share> getShares() {
        return shares;
    }

    public void setShares(Map<String, Share> shares) {
        this.shares = shares;
    }

    public Map<String, Comment> getComments() {
        return comments;
    }

    public void setComments(Map<String, Comment> comments) {
        this.comments = comments;
    }

    public Post() {
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public Blob getPostImage() {
        return postImage;
    }

    public void setPostImage(Blob postImage) {
        this.postImage = postImage;
    }

    public void setLiked(Boolean liked) {
        isLiked = liked;
    }

    public Boolean getLiked() {
        return isLiked;
    }

    public Map<String, String> getLikes() {
        return likes;
    }

    public void setLikes(Map<String, String> likes) {
        this.likes = likes;
    }

    public void addLike(String UID) {
        likes.put(UID, Timestamp.now().toString());
    }

    public void removeLike(String UID) {
        likes.remove(UID);
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }


    @Override
    public int compareTo(Post o) {
        return o.getTime().compareTo(time);
    }
}
