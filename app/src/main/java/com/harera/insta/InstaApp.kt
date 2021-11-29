package com.harera.insta

import android.app.Application
import coil.Coil
import coil.ImageLoader
import com.harera.base.datastore.LocalStore
import com.harera.base.notifications.createMessagesNotificationChannel
import com.harera.insta.di.*
import com.harera.login.di.loginModule
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class InstaApp : Application() {

    @InternalAPI
    override fun onCreate() {
        super.onCreate()

        val localStore = LocalStore(this)

        startKoin() {
            androidLogger()
            androidContext(this@InstaApp)
            modules(
                RepoModule,
                FirebaseModule,
                dataStoreModule,
                ViewModel,
                coilModule,
                AppModule,
                loginModule,
                UtilsModule,
                serviceModule,
                dbModule,
            )
        }

        createMessagesNotificationChannel(applicationContext)

        Coil.setImageLoader(
            ImageLoader
                .invoke(this)
                .newBuilder()
                .dispatcher(Dispatchers.IO)
                .placeholder(R.drawable.insta)
                .build()
        )
    }
}