package com.deinega95.clientMqtt.utils

import com.deinega95.clientMqtt.di.App
import androidx.core.app.NotificationManagerCompat
import android.app.NotificationManager
import android.app.NotificationChannel
import android.content.Context.NOTIFICATION_SERVICE
import androidx.core.content.ContextCompat.getSystemService
import android.text.TextUtils
import android.os.Build
import com.deinega95.clientMqtt.services.MyFirebaseMessagingService.Companion.NOTIFICATION_CHANNEL_ID


object Utils {
    fun isNotificationEnabled(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!TextUtils.isEmpty(NOTIFICATION_CHANNEL_ID)) {
                val manager =
                    App.instance.baseContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

                val isEnable =
                    NotificationManagerCompat.from(App.instance.baseContext).areNotificationsEnabled()
                if (!isEnable) {
                    return false
                }

                val channel = manager.getNotificationChannel(NOTIFICATION_CHANNEL_ID)
                return if (channel != null) {
                    channel.importance > NotificationManager.IMPORTANCE_NONE
                } else {
                    true
                }
            }
            return false
        } else {
            return NotificationManagerCompat.from(App.instance.baseContext).areNotificationsEnabled()
        }
    }

}