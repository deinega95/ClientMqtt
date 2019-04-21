package com.deinega95.clientMqtt.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    val dateFormat by lazy {
        return@lazy SimpleDateFormat("dd MMMM, HH:mm", Locale("ru"))
    }
}

fun Long.toDate(): String {
    return DateUtils.dateFormat.format(Date(this))
}

