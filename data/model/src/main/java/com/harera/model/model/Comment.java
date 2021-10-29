package com.harera.model.model;

import com.google.firebase.Timestamp;

public final class Comment {
    public String uid;
    public String postId;
    public String commentId;
    public String comment;
    public Timestamp time;
    public String profileName;

    public Comment() {
    }

    public Comment(String uid, String postId, String comment, Timestamp time) {
        this.uid = uid;
        this.postId = postId;
        this.commentId = commentId;
        this.comment = comment;
        this.time = time;
        this.profileName = profileName;
    }
}