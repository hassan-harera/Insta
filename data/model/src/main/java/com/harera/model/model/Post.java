package com.harera.model.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public final class Post {
    public String postImageUrl;
    public Timestamp time;
    public String caption;
    public String postId;
    public String uid;
    public String profileName;
    public String profileImageUrl;
    public int likesNumber;
    public int commentsNumber;

    public Post(String postImageUrl, Timestamp time, String caption, String postId, String uid, String profileName, String profileImageUrl, int likesNumber, int commentsNumber) {
        this.postImageUrl = postImageUrl;
        this.time = time;
        this.caption = caption;
        this.postId = postId;
        this.uid = uid;
        this.profileName = profileName;
        this.profileImageUrl = profileImageUrl;
        this.likesNumber = likesNumber;
        this.commentsNumber = commentsNumber;
    }

    public Post() {
    }
}
