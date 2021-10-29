package com.harera.model.model;

import com.google.firebase.Timestamp;

public final class Message {
    public String from;
    public String to;
    public String message;
    public Timestamp time;

    public Message() {
    }

    public Message(String from, String to, String message, Timestamp time) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.time = time;
    }
}
