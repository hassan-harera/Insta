package com.harera.model.model;

import com.google.firebase.Timestamp;

public final class FollowRequest extends Notification {
    private static final int TYPE = 2;

    public String fromUid;
    public String toUid;
    public Timestamp time;
    public String id;

    public FollowRequest() {
        super(TYPE);
    }

    public FollowRequest(String fromUid, String toUid, Timestamp time, String id) {
        super(TYPE);
        this.fromUid = fromUid;
        this.toUid = toUid;
        this.time = time;
        this.id = id;
    }
}

