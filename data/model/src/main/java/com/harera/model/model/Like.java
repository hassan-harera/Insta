package com.harera.model.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;
import com.harera.base.utils.time.InstaDateSerializer;

import java.util.Date;

import kotlinx.serialization.Serializable;

@IgnoreExtraProperties
@Entity
@Serializable(with = InstaDateSerializer.class)
public final class Like extends Notification {
    private static final int TYPE = 1;

    public String profileName;
    public String postImageUrl;
    public String postId;
    public String likeId;
    public String uid;
    public Date time;
    @PrimaryKey
    @NonNull
    public int id;

    public Like() {
        super(TYPE);
    }

    @Ignore
    public Like(String profileName, String postImageUrl, String postId, String likeId, String uid, Date time) {
        super(TYPE);
        this.profileName = profileName;
        this.postImageUrl = postImageUrl;
        this.postId = postId;
        this.likeId = likeId;
        this.uid = uid;
        this.time = time;
    }

}