package Model;

import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.insta.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Blob;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;
import java.util.Objects;

public class Profile {

    private String name, email, bio;
    private Blob profilePic;
    private Map<String, Timestamp> friendRequests;

    public Map<String, Timestamp> getFriendRequests() {
        return friendRequests;
    }

    public void setFriendRequests(Map<String, Timestamp> friendRequests) {
        this.friendRequests = friendRequests;
    }

    public Profile() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Blob getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Blob profilePic) {
        this.profilePic = profilePic;
    }

    public void addFriendRequest(String UID) {
        friendRequests.put(UID, Timestamp.now());
    }
}
