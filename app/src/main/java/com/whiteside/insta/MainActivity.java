package com.whiteside.insta;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.whiteside.insta.databinding.ActivityMainBinding;
import com.whiteside.insta.ui.profile.LoginActivity;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private ActivityMainBinding bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bind = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        SecretData.initializeFirebase(this);

        auth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        viewSplashPage();
    }

    private void viewSplashPage() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkLogin();
            }
        }, 2000);
    }

    private void checkLogin() {
        if (auth.getCurrentUser() != null) {
            goFeed();
        } else {
            goLogin();
        }
    }

    private void goFeed() {
        Intent intent = new Intent(this, WallActivity.class);
        startActivity(intent);
        finish();
    }

    private void goLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}