package com.deinega95.clientMqtt.services

import android.util.Log
import com.deinega95.clientMqtt.storage.PrefsManager
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MqttService @Inject constructor(){

    @Inject
    lateinit var prefsManager: PrefsManager

    var client : MqttClient?=null

    var qos = 2
    var broker = "tcp://0.tcp.ngrok.io:19640"
    //var broker = "tcp://192.168.1.232:1883"
    var clientId = "Android"

    fun connect(callback:(isSuccess:Boolean, error:String?)->Unit){
        if (client?.isConnected == true) callback.invoke(true,null)
        try {
            client = MqttClient(prefsManager.getServer(), prefsManager.getClientId(), MemoryPersistence())
            val connOpts = MqttConnectOptions()
            connOpts.isCleanSession = true
            Log.e("!!!", "Connecting to broker: ${prefsManager.getServer()}")
            client?.connect(connOpts)
            Log.e("!!!", "Connected")
callback.invoke(true,null)

        } catch (me: MqttException) {
            var messageError = "Code =${me.reasonCode}; message=${me.message}; loc=${me.localizedMessage};" +
                    " cause=${me.cause};exce=$me"
            me.printStackTrace()
            callback.invoke(false, messageError)
        }

        client?.setCallback(object :MqttCallback{
            override fun messageArrived(topic: String?, message: MqttMessage?) {
              //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun connectionLost(cause: Throwable?) {
            //    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
            //    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    fun sendMessage(mess:String, topic:String){
        val message = MqttMessage(mess.toByteArray())
        message.qos = qos
        client?.publish(topic, message)
        Log.e("!!!", "Message published")
    }

    fun disconnect(){
                    client?.disconnect()
//            Log.e("!!!","Disconnected")
    }
}