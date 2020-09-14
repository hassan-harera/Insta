package com.example.insta;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class MyApplication extends Application {


    public static void setFirebase(){
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseDatabase.getInstance().setPersistenceCacheSizeBytes(40000000);
    }

}
