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
    var id: Long? = null,
    @SerializedName("time")
    var time: Long? = null,
    @SerializedName("startPeriod")
    var startPeriod: Long? = null,
    @SerializedName("endPeriod")
    var endPeriod: Long? = null,
    @SerializedName("topic")
    var topic: String? = null,
    @SerializedName("countAllPhotoByPeriod")
    var countAllPhotoByPeriod: Int? = null
)