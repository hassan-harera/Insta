package com.harera.model.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public final class OpenChat {
    public String uid;
    public String profileImageUrl;
    public String lastMessage;
    public Timestamp time;
    public String profileName;

    public OpenChat() {
    }

    public OpenChat(String uid, String profileImageUrl, String lastMessage, Timestamp time, String profileName) {
        this.uid = uid;
        this.profileImageUrl = profileImageUrl;
        this.lastMessage = lastMessage;
        this.time = time;
        this.profileName = profileName;
    }
}
