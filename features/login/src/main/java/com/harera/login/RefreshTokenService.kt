package com.harera.login

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.harera.base.datastore.LocalStore
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class RefreshTokenService : Service() {
    private var token: String? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        token = intent?.getStringExtra("token")
        refreshToken()
        return START_NOT_STICKY
    }

    private fun refreshToken() = runBlocking(Dispatchers.IO) {
        kotlin.runCatching {
            HttpClient(Android).get<String> {
//                TODO change url to the base url
                url(getString(R.string.insta_server_base_url).plus("/refresh-token"))
                headers.append(HttpHeaders.Authorization, "Bearer $token")
            }.let {
                applicationContext?.getSharedPreferences("user", Context.MODE_PRIVATE)?.edit()
                    ?.putString(LocalStore.TOKEN, it)?.apply()
            }
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}