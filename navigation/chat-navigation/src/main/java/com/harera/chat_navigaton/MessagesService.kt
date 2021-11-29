package com.harera.chat_navigaton

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.gson.Gson
import com.harera.base.R
import com.harera.base.base.Mapper.messageFromText
import com.harera.base.notifications.MESSAGE_CHANNEL_ID
import com.harera.base.notifications.createMessageNotification
import com.harera.chat.ServiceUtil
import com.harera.model.response.MessageResponse
import io.ktor.http.*
import okhttp3.*
import okio.ByteString

private const val TAG = "MessagesService"

object ACTIONS {
    const val START_SERVICE = "start service"
    const val ACTION = "action"
}


class MessagesService : Service() {

    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null
    private lateinit var webSocketClient: WebSocket

    private inner class ServiceHandler(looper: Looper) : Handler(looper) {

        override fun handleMessage(msg: Message) {
        }
    }

    override fun onCreate() {
        setupWebSocket()

        startForeground(
            1,
            NotificationCompat.Builder(applicationContext, MESSAGE_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_message_icon)
                .setContentTitle("Notification")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build()
        )//        HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND).apply {
//            start()
//
//            serviceLooper = looper
//            serviceHandler = ServiceHandler(looper)
//        }
    }

    private fun setupWebSocket() {
        val token = getSharedPreferences("user", MODE_PRIVATE).getString("token", "")

        val request = Request
            .Builder()
            .url("ws://192.168.1.15:5000/chat")
            .header(HttpHeaders.Authorization, "Bearer $token")
            .build()

        webSocketClient = client.newWebSocket(request, messagesWebSocketListener)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when (intent.action) {
            ServiceUtil.SEND_MESSAGE -> {
                webSocketClient.send(intent.extras!!.getString("message"))
            }
            ServiceUtil.NEW_MESSAGE -> {
                webSocketClient.send(intent.extras!!.getString("message"))
            }
        }
        startForeground(
            1,
            NotificationCompat.Builder(applicationContext, MESSAGE_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_message_icon)
                .setContentTitle("Notification")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build()
        )

//        when (intent.action) {
//            START_SERVICE -> {
//                serviceHandler?.sendMessage(Message().apply {
//                    data = Bundle().apply { putString(ACTION, START_SERVICE) }
//                })
//            }
//        }

        Log.d(TAG, "onStartCommand: ")
        Log.d(TAG, "onStartCommand: $intent")

        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        Log.d(TAG, "onBind: ")
        return null
    }

    override fun onDestroy() {
        stopForeground(true)
        stopSelf()
        Log.d(TAG, "onDestroy: ")
    }

    fun sendMessage(messageResponse: MessageResponse) {
        sendBroadcast(Intent(ServiceUtil.NEW_MESSAGE).apply {
            putExtra("message",
                Gson().toJson(messageResponse))
        })

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
