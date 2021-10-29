package com.harera.model.model;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.sql.Date;
@Entity
public final class FollowRelation {
    public String followerUid;
    public String followingUid;
    public Date date;
    public String followId;
    @PrimaryKey(autoGenerate = true)
    public int id;

    @Ignore
    public FollowRelation(String followerUid, String followingUid, Date date, String followId) {
        this.followerUid = followerUid;
        this.followingUid = followingUid;
        this.date = date;
        this.followId = followId;
    }

    public FollowRelation() {
    }
}

