package Model;

import java.util.Date;

public class Notification implements Comparable<Notification>{
    private int id;
    private String UID, message;
    private String type, postID;
    private Date Date;

    public void setDate(java.util.Date date) {
        Date = date;
    }

    public java.util.Date getDate() {
        return Date;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUID() {
        return UID;
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }

    @Override
    public int compareTo(Notification o) {
        return o.getDate().compareTo(getDate());
    }
}
