package com.deinega95.clientMqtt.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.DEFAULT_ALL
import com.deinega95.clientMqtt.R
import com.deinega95.clientMqtt.di.App
import com.deinega95.clientMqtt.utils.MyLog
import com.deinega95.clientMqtt.utils.Utils
import com.deinega95.clientMqtt.view.activities.MainActivity
import com.deinega95.clientMqtt.view.activities.MainActivity.Companion.FROM_NOTIFICATION
import com.deinega95.clientMqtt.view.activities.MainActivity.Companion.TIME_PUSH
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    companion object {
        internal const val NOTIFICATION_CHANNEL_ID = "com.deinega95.clientMqtt.channel.one"
        internal const val CHANNEL_PUSH = "Channel One"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        MyLog.show("push = " + remoteMessage!!.data.toString())
        if (Utils.isNotificationEnabled()) {
            val mes = remoteMessage.data["text"] ?: ""
            val time = remoteMessage.data["time"] ?: ""
            showNotification(mes, time)
        }
    }

    private fun showNotification(text: String, time: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(FROM_NOTIFICATION, true)
        intent.putExtra(TIME_PUSH, time)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        var builder: NotificationCompat.Builder?

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                CHANNEL_PUSH, NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.setShowBadge(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableLights(true)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            if (notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID) == null)
                notificationManager.createNotificationChannel(notificationChannel)

            builder = NotificationCompat.Builder(applicationContext, notificationChannel.id)
        } else {
            builder = NotificationCompat.Builder(applicationContext)
        }

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        builder = builder
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.mipmap.ic_launcher))
            .setContentTitle(App.instance.getString(R.string.notification))
            .setContentText(text)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000, 1000, 1000))
            .setContentIntent(pendingIntent)
            .setSound(defaultSoundUri)
            .setDefaults(DEFAULT_ALL)
        try {
            notificationManager.notify(0, builder!!.build())
        } catch (ex: SecurityException) {
            builder!!.setSound(null)
            notificationManager.notify(0, builder.build())
        }
    }
}