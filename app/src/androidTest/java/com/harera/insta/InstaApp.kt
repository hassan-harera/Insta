package com.harera.insta

import android.app.Application
import com.harera.insta.di.*
import io.ktor.util.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class InstaApp : Application() {

    @InternalAPI
    override fun onCreate() {
        super.onCreate()
        startKoin() {
            androidContext(this@InstaApp)
            modules(
                RepoModule,
                FirebaseModule,
                ViewModel,
                AppModule,
                UtilsModule,
                ServiceModule,
                dataStoreModule,
            )
        }
    }

    override fun onTerminate() {
        stopKoin()
        super.onTerminate()
    }
}