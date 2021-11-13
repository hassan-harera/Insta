package com.harera.base.notifications

import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationCompat
import com.harera.base.R
import com.harera.model.response.MessageResponse


fun createMessageNotification(context: Context, message : MessageResponse): Notification {
    return NotificationCompat.Builder(context, MESSAGE_CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_message_icon)
        .setContentTitle("Message")
        .setContentText(message.message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
        .build()
}