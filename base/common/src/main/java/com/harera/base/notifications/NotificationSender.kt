package com.harera.base.notifications

import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.harera.base.R
import com.harera.model.response.MessageResponse


fun createMessageNotification(context: Context, message : MessageResponse): Notification {
    // Create an explicit intent for an Activity in your app
//            val intent = Intent(this, HomeAc::class.java).apply {
//                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            }
//            val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

    return NotificationCompat.Builder(context, MESSAGE_CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_message_icon)
        .setContentTitle("Message")
        .setContentText(message.message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
//        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .build()
}