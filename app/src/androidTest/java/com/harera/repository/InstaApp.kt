package com.harera.repository

import android.app.Application
import com.harera.repository.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class InstaApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin()  {
            androidContext(this@InstaApp)
            modules(
                AppModule,
            )
        }
    }

    override fun onTerminate() {
        stopKoin()
        super.onTerminate()
    }
}