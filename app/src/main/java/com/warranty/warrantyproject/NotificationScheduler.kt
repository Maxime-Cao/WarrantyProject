package com.warranty.warrantyproject

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import java.util.*

// Attention à l'endroit où doit se trouver cette classe (elle dépend de la vue => context)
class NotificationScheduler(private val context: Context?) {
    private val CHANNEL_ID : String = "warranty_notifications_channel";
    private val CHANNEL_NAME : String = "My Warranty Notification Channel"
    private val CHANNEL_DESCRIPTION : String = "This channel allows you to set up notifications that will serve as a reminder when a warranty expires"
    private val TITLE_NOTIFICATION : String = "End of validity of a warranty"

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH).apply {
                description = CHANNEL_DESCRIPTION
            }
            val notificationManager: NotificationManager =
                context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun scheduleNotification(notificationId:Int,message:String,dayInMilliseconds: Long) {

        val calendar = Calendar.getInstance()

        if(dayInMilliseconds > calendar.timeInMillis) {
            val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val notificationIntent = Intent(context,NotificationReceiver::class.java)

            notificationIntent.putExtra("title",TITLE_NOTIFICATION)
            notificationIntent.putExtra("message",message)

            val pendingIntent = PendingIntent.getBroadcast(context,notificationId,notificationIntent,PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

            alarmManager.setExact(AlarmManager.RTC_WAKEUP,dayInMilliseconds,pendingIntent)
        }

    }

    fun checkIfNotificationExists(id: Long): Boolean {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val notificationIntent = Intent(context,NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context,id.toInt(),notificationIntent,PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE)
        return pendingIntent != null
    }

    fun updateNotification(id: Int, message: String, time: Long) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val notificationIntent = Intent(context,NotificationReceiver::class.java)
        notificationIntent.putExtra("title",TITLE_NOTIFICATION)
        notificationIntent.putExtra("message",message)

        val pendingIntent = PendingIntent.getBroadcast(context,id.toInt(),notificationIntent,PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,time,pendingIntent)
    }

    fun getNotification(id: Long, dateOfExpiry: Date) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val notificationIntent = Intent(context,NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context,id.toInt(),notificationIntent,PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE)
        if(pendingIntent != null) {
            val longOfDateOfExpiry = dateOfExpiry.time
            Log.d("NotificationScheduler","longOfDateOfExpiry : $longOfDateOfExpiry")
            //Je veux pouvoir récupérer la date de notification de la notification avec l'id passé en paramètre
            val longOfDateOfNotification = alarmManager.nextAlarmClock?.triggerTime
            Log.d("NotificationScheduler","longOfDateOfNotification : $longOfDateOfNotification")

            val long = longOfDateOfNotification?.minus(longOfDateOfExpiry)
        }
    }

    fun cancelNotification(id: Int) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val notificationIntent = Intent(context,NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context,id,notificationIntent,PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE)
        if(pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
        }

    }

}