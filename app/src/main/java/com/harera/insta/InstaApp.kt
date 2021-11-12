package com.harera.insta

import android.app.Application
import android.content.Intent
import android.os.Build
import com.harera.base.notifications.createMessagesNotificationChannel
import com.harera.chat_navigaton.MessagesService
import com.harera.insta.di.*
import io.ktor.util.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class InstaApp : Application() {

    @InternalAPI
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
                UtilsModule,
                ServiceModule,
                dataStoreModule,
            )
            createMessagesNotificationChannel(baseContext)
//            startForegroundService(Intent(baseContext, MessagesService::class.java))

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(Intent(baseContext, MessagesService::class.java))
            } else {
                startService(Intent(baseContext, MessagesService::class.java))
            }
        }
    }
}