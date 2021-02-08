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

        initializeFirebase();

        auth = FirebaseAuth.getInstance();
    }

    private void initializeFirebase() {
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(getApplicationContext(),
                    new FirebaseOptions.Builder()
                            .setProjectId("insta-simulator")
                            .setApiKey("AIzaSyBQACODJwGDU-48H-UBD1Qpz-OCvT99f1M")
                            .setApplicationId("1:1004201569665:android:4de00e7c3db46075088c80")
                            .build());
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            FirebaseDatabase.getInstance().setPersistenceCacheSizeBytes(50000000);
        }
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