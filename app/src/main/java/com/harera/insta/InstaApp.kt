package com.harera.insta

import android.app.Application
import com.harera.insta.di.AppModule
import com.harera.insta.di.FirebaseModule
import com.harera.insta.di.UtilsModule
import com.harera.insta.di.ViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class InstaApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin() {
            androidLogger()
            androidContext(this@InstaApp)
            modules(
                AppModule,
                FirebaseModule,
                ViewModel,
                UtilsModule
            )
        }
    }
}