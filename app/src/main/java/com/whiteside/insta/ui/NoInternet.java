package com.whiteside.insta.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.whiteside.insta.R;

import Controller.Connection;

public class NoInternet extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);
    }

    public void checkInternet(View view) {
        if(Connection.isConnected(this)){
            finish();
        }
    }
}