package com.harera.insta

import android.app.Application
import com.harera.insta.di.*
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
                RepoModule,
                FirebaseModule,
                ViewModel,
                AppModule,
                UtilsModule
            )
        }
    }
}