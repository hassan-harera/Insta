package com.harera.model.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.harera.base.utils.time.InstaDateSerializer;

import java.sql.Date;

import kotlinx.serialization.Serializable;

@Entity
@Serializable(with = InstaDateSerializer.class)

public final class FollowRequest extends Notification {
    private static final int TYPE = 2;

    public String fromUid;
    public String toUid;
    public Date time;
    @NonNull
    @PrimaryKey
    public String id;

    public FollowRequest() {
        super(TYPE);
    }

    @Ignore
    public FollowRequest(String fromUid, String toUid, Date time, @NonNull String id) {
        super(TYPE);
        this.fromUid = fromUid;
        this.toUid = toUid;
        this.time = time;
        this.id = id;
    }
}

