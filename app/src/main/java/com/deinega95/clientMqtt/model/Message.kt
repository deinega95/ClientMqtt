package com.deinega95.clientMqtt.model

data class Message (
    var type:String?=null,
    var text:String?=null,
    var image:ByteArray?=null,
    var id:Int?=null,
    var time:Long?=null
)