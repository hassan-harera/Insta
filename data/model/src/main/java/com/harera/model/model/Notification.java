package com.harera.model.model;


import androidx.room.Entity;

public class Notification {
    public int type;

    public Notification(int type) {
        this.type = type;
    }
}