package com.harera.model.model;


import com.google.firebase.Timestamp;

public final class FollowRelation {
    public String followerUid;
    public String followingUid;
    public Timestamp timestamp;
    public String followId;


    public FollowRelation(String followerUid, String followingUid, Timestamp timestamp, String followId) {
        this.followerUid = followerUid;
        this.followingUid = followingUid;
        this.timestamp = timestamp;
        this.followId = followId;
    }

    public FollowRelation() {
    }
}

