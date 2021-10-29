package com.harera.model.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
@Entity
public final class Profile {
    public String name;
    public String email;
    public String bio;
    @PrimaryKey
    @NonNull
    public String uid;
    public String profileImageUrl;

    @Ignore
    public Profile(String name, String email, String bio, @NonNull String uid, String profileImageUrl) {
        this.name = name;
        this.email = email;
        this.bio = bio;
        this.uid = uid;
        this.profileImageUrl = profileImageUrl;
    }

    public Profile() {
    }
}