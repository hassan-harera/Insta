package com.harera.chat_navigaton

import android.app.Service
import android.content.Intent
import android.os.*
import android.os.Process.THREAD_PRIORITY_BACKGROUND
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.harera.base.R
import com.harera.base.base.Mapper.messageFromText
import com.harera.base.notifications.MESSAGE_CHANNEL_ID
import com.harera.base.notifications.createMessageNotification
import com.harera.model.response.MessageResponse
import io.ktor.http.*
import okhttp3.*
import okio.ByteString

private const val TAG = "MessagesService"


class MessagesService(

) : Service() {

    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null

    private inner class ServiceHandler(looper: Looper) : Handler(looper) {

        override fun handleMessage(msg: Message) {
            try {
                val request = Request
                    .Builder()
                    .url("ws://192.168.1.15:8080/chat")
                    .header(HttpHeaders.Authorization,
                        "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJodHRwOi8vMC4wLjAuMDo4MDgwL2hlbGxvIiwiaXNzIjoiaHR0cDovLzAuMC4wLjA6ODA4MC8iLCJleHAiOjE2Mzg2MDA1NjQsInVzZXJuYW1lIjoiMSJ9.PV1isIjteTstHmkectYQSikXVeYF4UNny9TZuO2Z4gM")
                    .build()

                client.newWebSocket(request, messagesWebSocketListener)

            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }

            stopSelf(msg.arg1)
        }
    }

    override fun onCreate() {
        Log.d(TAG, "onCreate: ")
        HandlerThread("ServiceStartArguments", THREAD_PRIORITY_BACKGROUND).apply {
            start()

            serviceLooper = looper
            serviceHandler = ServiceHandler(looper)
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        startForeground(
            startId,
            NotificationCompat.Builder(applicationContext, MESSAGE_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_message_icon)
                .setContentTitle("Notification")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build()
        )

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForegroundService(intent)
//        } else {
//            startService(intent)
//        }

        Log.d(TAG, "onStartCommand: ")
        Log.d(TAG, "onStartCommand: $intent")

        serviceHandler?.obtainMessage()?.also { msg ->
            msg.arg1 = startId
            serviceHandler?.sendMessage(msg)
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        Log.d(TAG, "onBind: ")
        return null
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: ")
    }

    fun sendMessage(messageResponse: MessageResponse) {
        createMessageNotification(
            context = baseContext,
            message = messageResponse
        ).let {
            with(NotificationManagerCompat.from(this)) {
                notify(messageResponse.messageId, it)
            }
        }
    }

    private companion object {
        val client = OkHttpClient()
    }

    private val messagesWebSocketListener = object : WebSocketListener() {
        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            Log.d(TAG, "onMessage: ")
            Log.d(TAG, "onMessage: $text")

            sendMessage(messageFromText(text))
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            super.onMessage(webSocket, bytes)
            Log.d(TAG, "onMessage: ")
            Log.d(TAG, "onMessage: ${bytes.size}")
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosing(webSocket, code, reason)
            Log.d(TAG, "onClosing: ")
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
            Log.d(TAG, "onClosed: ")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
            Log.d(TAG, "onFailure: ${t.message}")
        }

        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            Log.d(TAG, "onOpen: ")
        }
    }
}
