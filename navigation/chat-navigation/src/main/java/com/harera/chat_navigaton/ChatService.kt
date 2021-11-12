//package fr.tguerin.websocket.maze.service
//
//import android.app.Service
//import android.content.Intent
//import android.os.*
//import de.greenrobot.event.EventBus
//import fr.tguerin.websocket.maze.event.MessageReceivedEvent
//import fr.tguerin.websocket.maze.event.SendMessageEvent
//import okhttp3.WebSocketListener
//import okio.Buffer
//import okio.BufferedSource
//import timber.log.Timber
//import java.io.IOException
//
//class WebSocketClient : Service(), WebSocketListener {
//    private var mServiceHandler: Handler? = null
//    private var mServiceLooper: Looper? = null
//    private var mWebSocket: WebSocket? = null
//    private var mConnected = false
//
//    private inner class ServiceHandler(looper: Looper?) : Handler(looper!!) {
//        override fun handleMessage(msg: Message) {
//            when (msg.what) {
//                CONNECT_TO_WEB_SOCKET -> connectToWebSocket()
//                SEND_MESSAGE -> sendMessageThroughWebSocket(msg.data.getString(KEY_MESSAGE))
//                CLOSE_WEB_SOCKET -> closeWebSocket()
//                DISCONNECT_LOOPER -> mServiceLooper!!.quit()
//            }
//        }
//    }
//
//    private fun sendMessageThroughWebSocket(message: String?) {
//        if (!mConnected) {
//            return
//        }
//        try {
//            mWebSocket.sendMessage(WebSocket.PayloadType.TEXT,
//                Buffer().write(message!!.toByteArray()))
//        } catch (e: IOException) {
//            Timber.d("Error sending message", e)
//        }
//    }
//
//    private fun connectToWebSocket() {
//        val okHttpClient: com.squareup.okhttp.OkHttpClient = com.squareup.okhttp.OkHttpClient()
//        val request: com.squareup.okhttp.Request = com.squareup.okhttp.Request.Builder()
//            .url("ws://192.168.0.25:5000")
//            .build()
//        mWebSocket = WebSocket.newWebSocket(okHttpClient, request)
//        try {
//            val response: com.squareup.okhttp.Response = mWebSocket.connect(this@WebSocketClient)
//            if (response.code() == 101) {
//                mConnected = true
//            } else {
//                Timber.d("Couldn't connect to WebSocket %s %s %s",
//                    response.code(),
//                    response.message(),
//                    response.body().string())
//            }
//        } catch (e: IOException) {
//            Timber.d("Couldn't connect to WebSocket", e)
//        }
//    }
//
//    private fun closeWebSocket() {
//        if (!mConnected) {
//            return
//        }
//        try {
//            mWebSocket.close(1000, "Goodbye, World!")
//        } catch (e: IOException) {
//            Timber.d("Failed to close WebSocket", e)
//        }
//    }
//
//    override fun onBind(intent: Intent): IBinder? {
//        return null
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//        val thread = HandlerThread("WebSocket service")
//        thread.start()
//        mServiceLooper = thread.looper
//        mServiceHandler = ServiceHandler(mServiceLooper)
//        mServiceHandler!!.sendEmptyMessage(CONNECT_TO_WEB_SOCKET)
//        EventBus.getDefault().register(this)
//    }
//
//    fun onEvent(sendMessageEvent: SendMessageEvent) {
//        if (!mWebSocket.isClosed()) {
//            val message = Message.obtain()
//            message.what = SEND_MESSAGE
//            val data = Bundle()
//            data.putString(KEY_MESSAGE, sendMessageEvent.message)
//            message.data = data
//            mServiceHandler!!.sendMessage(message)
//        }
//    }
//
//    override fun onDestroy() {
//        mServiceHandler!!.sendEmptyMessage(CLOSE_WEB_SOCKET)
//        mServiceHandler!!.sendEmptyMessage(DISCONNECT_LOOPER)
//        EventBus.getDefault().unregister(this)
//        super.onDestroy()
//    }
//
//    @Throws(IOException::class)
//    fun onMessage(payload: BufferedSource, type: WebSocket.PayloadType) {
//        if (type === WebSocket.PayloadType.TEXT) {
//            EventBus.getDefault().post(MessageReceivedEvent(payload.readUtf8()))
//            payload.close()
//        }
//    }
//
//    fun onClose(code: Int, reason: String?) {
//        mConnected = false
//        Timber.d("Websocket is closed %s %s", code, reason)
//    }
//
//    fun onFailure(e: IOException?) {
//        mConnected = false
//        Timber.d("Websocket is closed", e)
//    }
//
//    companion object {
//        private const val CONNECT_TO_WEB_SOCKET = 1
//        private const val SEND_MESSAGE = 2
//        private const val CLOSE_WEB_SOCKET = 3
//        private const val DISCONNECT_LOOPER = 4
//        private const val KEY_MESSAGE = "keyMessage"
//    }
//}