package com.deinega95.clientMqtt.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64


fun String.toBitmap(): Bitmap? {
    return try {
        val encodeByte = Base64.decode(this, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
    } catch (e: Exception) {
        e.message
        null
    }
}

