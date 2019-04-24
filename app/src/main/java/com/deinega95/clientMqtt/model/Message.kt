package com.deinega95.clientMqtt.model

import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("type")
    var type: String? = null,
    @SerializedName("text")
    var text: String? = null,
    @SerializedName("image")
    var image: String? = null,
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("time")
    var time: Long? = null,
    @SerializedName("startPeriod")
    var startPeriod: Long? = null,
    @SerializedName("endPeriod")
    var endPeriod: Long? = null,
    @SerializedName("topicByPeriodPhoto")
    var topicByPeriodPhoto: String? = null
)