package com.deinega95.clientMqtt.services

import android.util.Log
import com.deinega95.clientMqtt.di.App
import com.deinega95.clientMqtt.di.scopes.MainScope
import com.deinega95.clientMqtt.model.Message
import com.deinega95.clientMqtt.storage.PrefsManager
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@MainScope
class MqttHelper @Inject constructor() : Observable() {

    @Inject
    lateinit var prefsManager: PrefsManager

    private val TOPIC = "SMART-HOUSE-KUBSU"

    var client: MqttAndroidClient? = null
    private var messages = ArrayList<Message>()

    var broker = "tcp://0.tcp.ngrok.io:19640"
    //var broker = "tcp://192.168.1.232:1883"


    fun connect() {

        client =
            MqttAndroidClient(App.instance.applicationContext, prefsManager.getServer(), prefsManager.getClientId())

        client?.setCallback(object : MqttCallbackExtended {
            override fun connectComplete(reconnect: Boolean, serverURI: String?) {
                Log.e("**", "connectComplete")
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                Log.e("messageArrived", message.toString())
                messages.add(Message(message.toString()))

                setChanged()
                notifyObservers(messages)
            }

            override fun connectionLost(cause: Throwable?) {
                Log.e("**", "connectionLost")
                client?.connect()
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                Log.e("**", "deliveryComplete")
            }
        })

        val connOpts = MqttConnectOptions()
        connOpts.isCleanSession = false
        connOpts.isAutomaticReconnect = true

        client?.connect(connOpts, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                val disconnectedBufferOptions = DisconnectedBufferOptions()
                disconnectedBufferOptions.isBufferEnabled = true
                disconnectedBufferOptions.bufferSize = 100
                disconnectedBufferOptions.isPersistBuffer = false
                disconnectedBufferOptions.isDeleteOldestMessages = false
                client?.setBufferOpts(disconnectedBufferOptions)
                subscribeToTopic()
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {

                Log.e("!!!", "onFailure coonect")
            }
        })


    }

    private fun subscribeToTopic() {
        client?.subscribe("/testing", 0, null, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                Log.w("Mqtt", "Subscribed!");
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                Log.w("Mqtt", "Subscribed fail!");
            }
        })

    }

    fun sendMessage(message: String) {
        client!!.publish("/testing", MqttMessage(message.toByteArray()))
    }
}