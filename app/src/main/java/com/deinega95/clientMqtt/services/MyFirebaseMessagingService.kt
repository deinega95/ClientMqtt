package com.deinega95.clientMqtt.services

import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.deinega95.clientMqtt.R
import com.deinega95.clientMqtt.di.App
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    init {
        App.instance.component?.inject(this)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        Log.e("+push = ", remoteMessage!!.data.toString())

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        var builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext)

        val mes = remoteMessage.data["text"] ?: ""

        builder = builder
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.mipmap.ic_launcher))
            .setContentTitle("Оповещение")
            .setContentText(mes)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)

        notificationManager.notify(0, builder.build())
    }
}