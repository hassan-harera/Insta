package com.whiteside.insta

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class InstaApp : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}