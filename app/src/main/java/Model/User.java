package Model;

import android.graphics.Bitmap;


public class User {

    private String name, bio, uid, email;
    private Bitmap profilePic;

    public User() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProfilePic(Bitmap profilePic) {
        this.profilePic = profilePic;
    }

    public String getName() {
        return name;
    }

    public String getBio() {
        return bio;
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public Bitmap getProfilePic() {
        return profilePic;
    }
}
