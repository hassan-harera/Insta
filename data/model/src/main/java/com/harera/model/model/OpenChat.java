package com.harera.model.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.harera.base.utils.time.InstaDateSerializer;

import kotlinx.serialization.Serializable;

@IgnoreExtraProperties
@Entity
@Serializable(with = InstaDateSerializer.class)
public final class OpenChat {
    public String uid;
    public String profileImageUrl;
    public String lastMessage;
    public Date time;
    public String profileName;
    @PrimaryKey
    @NonNull
    public int id;

    public OpenChat() {
    }

    @Ignore
    public OpenChat(String uid, String profileImageUrl, String lastMessage, Date time, String profileName) {
        this.uid = uid;
        this.profileImageUrl = profileImageUrl;
        this.lastMessage = lastMessage;
        this.time = time;
        this.profileName = profileName;
    }
}
