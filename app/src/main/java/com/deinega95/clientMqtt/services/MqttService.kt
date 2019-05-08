package com.deinega95.clientMqtt.services

import com.deinega95.clientMqtt.di.App
import com.deinega95.clientMqtt.model.Message
import com.deinega95.clientMqtt.storage.DbManager
import com.deinega95.clientMqtt.storage.PrefsManager
import com.deinega95.clientMqtt.utils.MyLog
import com.google.gson.Gson
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MqttService @Inject constructor() : Observable() {

    companion object {
        const val TOPIC = "/smart-house/devices"
        const val SERVER_TOPIC = "/smart+-house/camera"
        const val PHOTO_BY_PERIOD_UPDATED = "PHOTO_BY_PERIOD_UPDATED"
    }

    @Inject
    lateinit var gson: Gson
    @Inject
    lateinit var prefsManager: PrefsManager
    @Inject
    lateinit var dbManager: DbManager

    val topics = mutableSetOf<String>()
    var client: MqttAndroidClient? = null
    val messages = mutableSetOf<Message>()

    val photoByPeriod = mutableListOf<Message>()
    var countAllPhotoByPeriod = 0
    private var topicPhotoByPeriod: String? = null

    //  val broker = "tcp://m16.cloudmqtt.com:12163"
    //   val broker = "tcp://192.168.1.232:1883"
    //var broker = "tcp://192.168.43.100:1883"

    fun connect(delegate: (isConnect: Boolean, error: String?) -> Unit = { _, _ -> }) {
        if (messages.isEmpty()) {
            val existMess = dbManager.getMessages()
            messages.addAll(existMess)
        }
        initClient()
        val options = createOptions()
        val listener = createMqttListener(delegate)
        client!!.connect(options, this, listener)
        setCallback()
    }

    private fun createMqttListener(delegate: (isConnect: Boolean, error: String?) -> Unit): IMqttActionListener {
        return object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                MyLog.show("client connect onSuccess")
                delegate.invoke(true, null)
                setDisconnectOptions()
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                MyLog.show("onFailure connect ${exception?.localizedMessage}")
                delegate.invoke(false, exception?.localizedMessage)
                exception?.printStackTrace()
            }
        }
    }

    private fun setDisconnectOptions() {
        val disconnectedBufferOptions = DisconnectedBufferOptions()
        disconnectedBufferOptions.isBufferEnabled = true
        disconnectedBufferOptions.bufferSize = 100
        disconnectedBufferOptions.isPersistBuffer = false
        disconnectedBufferOptions.isDeleteOldestMessages = false
        client?.setBufferOpts(disconnectedBufferOptions)
    }

    private fun setCallback() {
        client?.setCallback(object : MqttCallbackExtended {
            override fun connectComplete(reconnect: Boolean, serverURI: String?) {
                MyLog.show("connectComplete")
            }

            override fun messageArrived(topic: String?, mes: MqttMessage?) {
                MyLog.show("messageArrived ${mes.toString()}")
                try {
                    val message = gson.fromJson(mes.toString(), Message::class.java)!!
                    parseMessage(message)

                } catch (ex: Exception) {
                    MyLog.show("Exception in message ${ex.printStackTrace()}")
                    MyLog.show("Exception in message ${ex.localizedMessage}")
                }
            }

            override fun connectionLost(cause: Throwable?) {
                MyLog.show("connectionLost")
                client?.connect()
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                MyLog.show("deliveryComplete")
            }
        })
    }

    private fun parseMessage(message: Message) {
        if (topicPhotoByPeriod != null && message.topicForPhoto == topicPhotoByPeriod) {
            if (message.countAllPhotoByPeriod != null) {
                countAllPhotoByPeriod = message.countAllPhotoByPeriod!!
                setChanged()
                notifyObservers(countAllPhotoByPeriod)
            } else {
                photoByPeriod.add(message)
                setChanged()
                notifyObservers(PHOTO_BY_PERIOD_UPDATED)
            }
        } else {
            MyLog.show("!!!messages.contains(message)=${messages.contains(message)}'''${message.hashCode()}/// ${message.id}")
            dbManager.addMessage(message)
            messages.add(message)
            setChanged()
            notifyObservers(messages.toList())
        }
    }

    private fun initClient() {
        MyLog.show("broker=${prefsManager.getServer()}")
        val clietnId = MqttClient.generateClientId()
        client = MqttAndroidClient(
            App.instance.applicationContext,
            prefsManager.getServer(),
            clietnId
        )
    }

    private fun createOptions(): MqttConnectOptions {
        val options = MqttConnectOptions()
        options.mqttVersion = MqttConnectOptions.MQTT_VERSION_3_1
        options.isCleanSession = false
        prefsManager.getUsername()?.apply {
            options.userName = this
        }
        prefsManager.getPassword()?.apply {
            options.password = this.toCharArray()
        }
        options.isAutomaticReconnect = true
        MyLog.show("options = ${options.toString()}")
        return options
    }

    fun subscribeToTopic(topic: String, callback: (top: Set<String>) -> Unit = {}) {
        client?.subscribe(topic, 0, null, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                MyLog.show("Mqtt Subscribed topic")
                topics.add(topic)
                callback.invoke(topics)
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                MyLog.show("Mqtt onFailure Subscribed topic")
            }
        })
    }


    fun unsubscribeTopic(topic: String, callback: (top: Set<String>) -> Unit) {
        client?.unsubscribe(topic, this, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                topics.remove(topic)
                callback.invoke(topics)
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                callback.invoke(topics)
            }
        })
    }

    fun sendMessage(message: Message) {
        val mes = gson.toJson(message)
        MyLog.show("send mes")
        client!!.publish(TOPIC, mes.toByteArray(), 0, false)
    }

    fun sendFirebaseToken(token: String) {
        val tokenMes = Message()
        tokenMes.type = "tokenFirebase"
        tokenMes.text = token

        val message = gson.toJson(tokenMes)
        client!!.publish(SERVER_TOPIC, message.toByteArray(), 2, true)
    }


/*    fun sendImage(b: ByteArray?) {
        client!!.publish(TOPIC, MqttMessage("image@".toByteArray() + b!!))
    }*/

    fun clear() {
        countAllPhotoByPeriod = 0
        messages.clear()
        photoByPeriod.clear()
        topics.clear()
        client?.disconnect()
        client = null
        topicPhotoByPeriod = null
    }

    fun getCurrentPhoto() {
        val mes = Message().apply {
            type = "get_current_image"
        }
        val message = gson.toJson(mes)
        client!!.publish(SERVER_TOPIC, message.toByteArray(), 2, true, this, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                MyLog.show("publish getImage onSuccess")
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                MyLog.show("publish getImage onFailure")
            }
        })
    }

    fun getPhotoByPeriod(topicWithPhoto: String, startTime: Long, endTime: Long) {
        photoByPeriod.clear()
        topicPhotoByPeriod = topicWithPhoto

        val message = Message().apply {
            type = "get_photo_by_period"
            startPeriod = startTime
            endPeriod = endTime
            topicForPhoto = topicWithPhoto
        }
        val mes = gson.toJson(message)
        MyLog.show("send mes $mes")
        client!!.publish(SERVER_TOPIC, mes.toByteArray(), 0, true)

        subscribeToTopic(topicWithPhoto)
    }

    fun clearMessages() {
        messages.clear()
        setChanged()
        notifyObservers(messages.toList())
    }
}