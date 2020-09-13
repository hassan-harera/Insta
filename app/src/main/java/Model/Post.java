package Model;

import android.graphics.Bitmap;

import java.util.Date;
import java.util.List;

public class Post implements Comparable<Post>{

    private String caption, UID, date;;
    private int id;
    private Bitmap bitmap;
    private int likes;
    private Boolean isLiked;

    public void setLiked(Boolean liked) {
        isLiked = liked;
    }

    public Boolean getLiked() {
        return isLiked;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getLikes() {
        return likes;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }

    @Override
    public String toString() {
        return id + "";
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    @Override
    public int compareTo(Post o) {
        return new Date(o.date).compareTo(new Date(date));
    }
}
