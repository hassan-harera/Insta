package com.harera.model.model;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public final class Chat {
    public String firstUid;
    public String secondUid;

    public Chat(String firstUid, String secondUid) {
        this.firstUid = firstUid;
        this.secondUid = secondUid;
    }

    public Chat() {

    }
}
