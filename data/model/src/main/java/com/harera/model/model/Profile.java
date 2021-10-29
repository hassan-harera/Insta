package com.harera.model.model;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public final class Profile {
    public String name;
    public String email;
    public String bio;
    public String uid;
    public String profileImageUrl;

    public Profile(String name, String email, String bio, String uid, String profileImageUrl) {
        this.name = name;
        this.email = email;
        this.bio = bio;
        this.uid = uid;
        this.profileImageUrl = profileImageUrl;
    }

    public Profile() {
    }
}