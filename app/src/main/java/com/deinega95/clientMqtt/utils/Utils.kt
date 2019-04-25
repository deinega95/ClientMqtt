package com.deinega95.clientMqtt.utils

import android.app.NotificationManager
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import android.text.TextUtils
import androidx.core.app.NotificationManagerCompat
import com.deinega95.clientMqtt.di.App
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

    fun getDataURL(imgFile: String): String {
        return ("data:image/jpeg;base64,$imgFile")
    }

}