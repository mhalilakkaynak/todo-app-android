package com.mha.todo.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.mha.todo.R
import com.mha.todo.view.MainActivity

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val contentText = intent!!.extras!!["contentText"] as String
        val notePosition = intent.extras!!["notePosition"] as Int
        val alarmUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val ringtone: Ringtone = RingtoneManager.getRingtone(context, alarmUri)
        val build: NotificationCompat.Builder
        val notificationManager =
            context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationIntent = Intent(context, MainActivity::class.java)
        notificationIntent.putExtra("notePosition", notePosition)
        val pendingIntent =
            PendingIntent.getActivity(context,
                1,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "channelId"
            val channelName = "channelName"
            val channelDescription = "channelDescription"
            val channelImportance = NotificationManager.IMPORTANCE_HIGH
            var channel = notificationManager.getNotificationChannel(channelId)
            if (channel == null) {
                channel = NotificationChannel(channelId, channelName, channelImportance)
                channel.description = channelDescription
                notificationManager.createNotificationChannel(channel)
            }
            build = NotificationCompat.Builder(context, channelId)
                .setContentTitle("Todo list")
                .setContentText(contentText)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher_todolistnotification_foreground)
                .setContentIntent(pendingIntent)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
        } else {
            build = NotificationCompat.Builder(context)
                .setContentTitle("Todo list")
                .setContentText(contentText)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher_todolistnotification_foreground)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true).setPriority(Notification.PRIORITY_HIGH)
        }
        notificationManager.notify(1, build.build())
        ringtone.play()
    }
}