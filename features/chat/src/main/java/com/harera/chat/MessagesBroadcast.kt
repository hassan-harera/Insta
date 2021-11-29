package com.harera.chat

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MessagesBroadcast(
    private val onNewMessageReceived: (String) -> Unit,
) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        onNewMessageReceived(intent?.extras?.getString("message") ?: "message")
    }
}