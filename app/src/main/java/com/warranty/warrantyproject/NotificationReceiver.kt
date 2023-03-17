package com.warranty.warrantyproject

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class NotificationReceiver : BroadcastReceiver() {
    private val NOTIFICATION_ID = "id"
    private val CHANNEL_ID : String = "warranty_notifications_channel"
    private val TITLE_EXTRA : String = "title"
    private val MESSAGE_EXTRA : String = "message"

    override fun onReceive(context: Context, intent: Intent) {
        val notification = NotificationCompat.Builder(context,CHANNEL_ID)
            .setSmallIcon(androidx.core.R.drawable.notification_icon_background)
            .setContentTitle(intent.getStringExtra(TITLE_EXTRA))
            .setContentText(intent.getStringExtra(MESSAGE_EXTRA))
            .build()

        val notificationId = intent.getIntExtra(NOTIFICATION_ID,0)
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId,notification)
        NotificationScheduler(context).cancelNotification(notificationId)
    }
}