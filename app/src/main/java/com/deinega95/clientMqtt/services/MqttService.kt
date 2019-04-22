package com.deinega95.clientMqtt.services

import com.deinega95.clientMqtt.di.App
import com.deinega95.clientMqtt.model.Message
import com.deinega95.clientMqtt.storage.PrefsManager
import com.deinega95.clientMqtt.utils.MyLog
import com.google.gson.Gson
import com.google.gson.JsonParseException
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import java.lang.Exception
import java.nio.charset.Charset
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MqttService @Inject constructor() : Observable() {

    companion object {
        const val TOPIC = "/smart-house/devices"
        const val SERVER_TOPIC = "/smart-house/camera"
    }

    @Inject
    lateinit var gson: Gson
    @Inject
    lateinit var prefsManager: PrefsManager

    val topics = mutableSetOf<String>()
    var client: MqttAndroidClient? = null
    private var messages = ArrayList<Message>()

    //  val broker = "tcp://m16.cloudmqtt.com:12163"
    //   val broker = "tcp://192.168.1.232:1883"
    //var broker = "tcp://192.168.43.100:1883"

    fun connect(delegate: (isConnect: Boolean, error: String?) -> Unit = { _, _ -> }) {
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
    val message = gson.fromJson(mes.toString(), Message::class.java)
    messages.add(message)
} catch (ex:Exception){
    MyLog.show("Exception in message")
    val mes = Message().apply {
        image = mes?.payload
        type="image"
    }
    messages.add(mes)
}


                setChanged()
                notifyObservers(messages)
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


    fun unsubscribeTopic(topic: String, callback:(top: Set<String>)->Unit) {
        client?.unsubscribe(topic,this, object : IMqttActionListener{
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
        client!!.publish(TOPIC, mes.toByteArray(), 2, true)
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

    fun clear(){
        topics.clear()
        client?.disconnect()
        client = null
    }

    fun getCurrentPhoto() {
        val mes = Message().apply {
            type = "get_current_image"
        }
        val message = gson.toJson(mes)
        client!!.publish(SERVER_TOPIC, message.toByteArray(), 2, true, this, object :IMqttActionListener{
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                MyLog.show("publish getImage onSuccess")
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                MyLog.show("publish getImage onFailure")
            }
        })
    }

}