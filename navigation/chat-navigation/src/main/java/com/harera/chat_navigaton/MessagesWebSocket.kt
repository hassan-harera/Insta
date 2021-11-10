//package com.harera.chat_navigaton
//
//import android.content.Intent
//import android.app.IntentService
//import java.lang.Exception
//
//
//class MessagesWebSocket {
//}
//
//class WebSocketServices : IntentService("DownloadService") {
//    private var message: String? = null
//    private var socket: WebSocket? = null
//    override fun onHandleIntent(intent: Intent?) {
//        Log.i(TAG, "onHandleIntent")
//
//        //SOCKET_ADDR = intent.getStringExtra("ip");
//        if (socket == null || !socket.isOpen() || socket.isPaused()) connectToPASocket(SOCKET_ADDR)
//        Log.d(TAG, "Service Invoke Function")
//    } //end onHandleIntent
//
//    //=====================================================================================
//    // Socket connection
//    //=====================================================================================
//    private fun connectToPASocket(SOCKET_ADDR: String) {
//        Log.i(TAG, "connectToPASocket()")
//
//        // Checking
//        if (socket != null && socket.isOpen()) return
//
//
//        // Initiate web socket connection
//        AsyncHttpClient.getDefaultInstance().websocket(SOCKET_ADDR, null,
//            object : WebSocketConnectCallback() {
//                fun onCompleted(ex: Exception?, webSocket: WebSocket?) {
//                    Log.i(TAG, "onCompleted")
//                    if (ex != null) {
//                        Log.i(TAG, "onCompleted > if (ex != null)")
//                        ex.printStackTrace()
//                        return
//                    }
//                    socket = webSocket
//                    socket.setStringCallback(object : StringCallback() {
//                        fun onStringAvailable(s: String) {
//                            Log.i(TAG,
//                                "socket.setStringCallback > onStringAvailable - s => $s")
//                            println("I got a string: $s")
//                            message = s
//                        } // end onStringAvailable
//                    }) // end socket.setStringCallback
//                    socket.setDataCallback(object : DataCallback() {
//                        // Find out what this does
//                        fun onDataAvailable(emitter: DataEmitter, bb: ByteBufferList) {
//                            Log.i(TAG,
//                                "socket.setDataCallback > onDataAvailable | emitter=> $emitter | bb => $bb")
//                            println("I got some bytes!")
//                            // note that this data has been read
//                            bb.recycle()
//                        }
//                    }) // end webSocket.setDataCallback
//                } // end onCompleted
//            }) // end AsyncHttpClient.getDefaultInstance()
//    } // end connectToPASocket
//
//    companion object {
//        val TAG = WebSocketServices::class.java.simpleName
//        private const val SOCKET_ADDR = "http://171.0.0.1:8080"
//    }
//} //end WebSocketServices
