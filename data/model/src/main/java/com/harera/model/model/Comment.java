package com.harera.model.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.ServerTimestamp;
import com.harera.base.utils.time.InstaDateSerializer;

import java.util.Date;

import kotlinx.serialization.Serializable;

@Entity
@Serializable(with = InstaDateSerializer.class)
public final class Comment {
    public String uid;
    public String postId;
    public String commentId;
    public String comment;
    public Date time;
    public String profileName;
    @PrimaryKey
    @NonNull
    public int id;

    public Comment() {
    }

    @Ignore
    public Comment(String uid, String postId, String comment, Date time) {
        this.uid = uid;
        this.postId = postId;
        this.commentId = commentId;
        this.comment = comment;
        this.time = time;
        this.profileName = profileName;
    }
}