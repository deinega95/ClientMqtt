package com.deinega95.clientMqtt.presenter

import com.deinega95.clientMqtt.di.scopes.MainScope
import com.deinega95.clientMqtt.model.Message
import com.deinega95.clientMqtt.services.MqttHelper
import com.deinega95.clientMqtt.storage.PrefsManager
import com.deinega95.clientMqtt.view.fragments.interfaces.ITopicFragment
import java.util.*
import javax.inject.Inject

@MainScope
class TopicPresenter @Inject constructor() : BasePresenter<ITopicFragment>(), Observer {
    @Inject
    lateinit var client: MqttHelper

    @Inject
    lateinit var prefsManager: PrefsManager

/*    var mImagesProgressHandler = object : android.os.Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: android.os.Message?) {

            Log.e("+++,", "handle mess ${msg?.what}")
            view?.updateAdapter()

            Log.e("+++,", "handle mess ${msg?.obj}")
            view?.setMessage(msg!!.obj as ArrayList<Message>)
        }
    }*/


    override fun viewAttached() {
        client.connect()
        client.addObserver(this)
    }


    override fun viewDettached() {
        client.deleteObserver(this)
    }


    fun onSendClicked(message: String) {
        client.sendMessage(message)

    }

    override fun update(o: Observable?, arg: Any?) {
        if (arg is ArrayList<*>)
            view?.setMessage(arg as ArrayList<Message>)

    }
}