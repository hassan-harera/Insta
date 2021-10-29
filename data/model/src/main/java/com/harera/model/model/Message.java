package com.harera.model.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.harera.base.utils.time.InstaDateSerializer;

import java.util.Date;

import kotlinx.serialization.Serializable;

@Entity
@Serializable(with = InstaDateSerializer.class)
public final class Message {
    public String from;
    public String to;
    public String message;
    public Date time;
    @PrimaryKey
    @NonNull
    public int id;

    public Message() {
    }

    @Ignore
    public Message(String from, String to, String message, Date time) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.time = time;
    }
}
