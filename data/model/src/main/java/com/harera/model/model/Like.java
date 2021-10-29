package com.harera.model.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public final class Like extends Notification {
    private static final int TYPE = 1;

    public String profileName;
    public String postImageUrl;
    public String postId;
    public String likeId;
    public String uid;
    public Timestamp time;

    public Like() {
        super(TYPE);
    }

    public Like(String profileName, String postImageUrl, String postId, String likeId, String uid, Timestamp time) {
        super(TYPE);
        this.profileName = profileName;
        this.postImageUrl = postImageUrl;
        this.postId = postId;
        this.likeId = likeId;
        this.uid = uid;
        this.time = time;
    }

}