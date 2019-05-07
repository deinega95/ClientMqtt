package com.deinega95.clientMqtt.model

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Message : RealmObject() {
    @PrimaryKey
    @SerializedName("id")
    var id: Long? = null

    @SerializedName("type")
    var type: String? = null

    @SerializedName("text")
    var text: String? = null

    @SerializedName("image")
    var image: String? = null

    @SerializedName("time")
    var time: Long? = null

    @SerializedName("startPeriod")
    var startPeriod: Long? = null

    @SerializedName("endPeriod")
    var endPeriod: Long? = null

    @SerializedName("topicForPhoto")
    var topicForPhoto: String? = null

    @SerializedName("countAllPhotoByPeriod")
    var countAllPhotoByPeriod: Int? = null


    override fun hashCode(): Int {
        return id!!.toInt()
    }

    override fun equals(other: Any?): Boolean {
        return if (other is Message && hashCode() == other.hashCode()) {
            other.id == id
        } else {
            false
        }
    }
}