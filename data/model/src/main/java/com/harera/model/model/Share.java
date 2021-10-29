package com.harera.model.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public final class Share {
    public String uid;
    public String postId;
    public String shareCaption;
    public Timestamp time;


    public Share(String uid, String postId, String shareCaption, Timestamp time) {
        this.uid = uid;
        this.postId = postId;
        this.shareCaption = shareCaption;
        this.time = time;
    }

    public Share() {
    }
}