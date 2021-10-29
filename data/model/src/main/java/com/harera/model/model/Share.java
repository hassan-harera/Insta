package com.harera.model.model;

import androidx.room.Entity;
import androidx.room.Ignore;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.harera.base.utils.time.InstaDateSerializer;

import java.util.Date;

import kotlinx.serialization.Serializable;

@IgnoreExtraProperties
@Entity
@Serializable(with = InstaDateSerializer.class)
public final class Share {
    public String uid;
    public String postId;
    public String shareCaption;
    public Date time;

    @Ignore
    public Share(String uid, String postId, String shareCaption, Date time) {
        this.uid = uid;
        this.postId = postId;
        this.shareCaption = shareCaption;
        this.time = time;
    }

    public Share() {
    }
}