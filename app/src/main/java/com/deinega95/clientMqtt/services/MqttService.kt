package com.deinega95.clientMqtt.services

import com.deinega95.clientMqtt.di.App
import com.deinega95.clientMqtt.model.Message
import com.deinega95.clientMqtt.storage.PrefsManager
import com.deinega95.clientMqtt.utils.MyLog
import com.google.gson.GsonBuilder
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MqttService @Inject constructor() : Observable() {

    companion object {
        const val TOPIC = "/smart-house/camera"
    }

    @Inject
    lateinit var prefsManager: PrefsManager

    private val gson by lazy { GsonBuilder().create() }
    val topics = mutableListOf<String>()

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
                MyLog.show("messageArrived ${mes!!.payload.toString()}")

                val message = gson.fromJson(mes.toString(), Message::class.java)
                messages.add(message)

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

    fun subscribeToTopic(topic: String, callback: (top: List<String>) -> Unit = {}) {
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

    fun sendMessage(message: String) {

        val mes = gson.toJson(message)
        MyLog.show("send mes")
        client!!.publish(TOPIC, mes.toByteArray(), 2, true)
    }


/*    fun sendImage(b: ByteArray?) {
        client!!.publish(TOPIC, MqttMessage("image@".toByteArray() + b!!))
    }*/
}