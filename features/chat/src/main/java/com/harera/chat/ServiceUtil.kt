package com.harera.chat

import android.app.ActivityManager
import android.content.Context

object ServiceUtil {

    const val NEW_MESSAGE = "message.new.message"
    const val SEND_MESSAGE = "message.send"

    fun isServiceRunning(strServiceName: String, context: Context): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (strServiceName == service.service.className) {
                return true
            }
        }
        return false
    }
}