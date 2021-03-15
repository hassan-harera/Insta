package com.whiteside.insta.ui.main

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.FirebaseDatabase
import com.whiteside.insta.SecretData

class MainViewModel(application: Application) : AndroidViewModel(application) {

    fun initializeFirebase(context: Context) {
        if (FirebaseApp.getApps(context).isEmpty()) {
            FirebaseApp.initializeApp(context,
                FirebaseOptions.Builder()
                    .setProjectId("insta-simulator")
                    .setApiKey(SecretData.FIREBASE_WEB_API_KEY)
                    .setApplicationId(SecretData.FIREBASE_APP_ID)
                    .build())
            FirebaseDatabase.getInstance().setPersistenceEnabled(true)
            FirebaseDatabase.getInstance().setPersistenceCacheSizeBytes(50000000)
        }
    }
}