package com.deinega95.clientMqtt.presenter

import android.util.Log
import com.deinega95.clientMqtt.R
import com.deinega95.clientMqtt.di.App
import com.deinega95.clientMqtt.di.scopes.MainScope
import com.deinega95.clientMqtt.model.Message
import com.deinega95.clientMqtt.services.MqttService
import com.deinega95.clientMqtt.services.MqttService.Companion.TOPIC
import com.deinega95.clientMqtt.services.ViewRouter
import com.deinega95.clientMqtt.storage.PrefsManager
import com.deinega95.clientMqtt.utils.MyLog
import com.deinega95.clientMqtt.view.fragments.interfaces.ITopicFragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import java.util.*
import javax.inject.Inject

@MainScope
class TopicPresenter @Inject constructor() : BasePresenter<ITopicFragment>(), Observer {
    @Inject
    lateinit var client: MqttService
    @Inject
    lateinit var prefsManager: PrefsManager
    @Inject
    lateinit var viewRouter: ViewRouter


    override fun viewAttached() {
        client.connect { isConnect, error ->
            if (isConnect) {
                client.addObserver(this)
                client.subscribeToTopic(TOPIC) { topics ->
                    view?.showTopics(topics.toList())
                }
                sendFirebaseToken()
            } else {
                viewRouter.showError(error)
            }
        }
    }

    private fun sendFirebaseToken() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }

                val token = task.result?.token
                Log.d("!!", token)
                if (token != null) {
                    client.sendFirebaseToken(token)
                }
            })
    }


    override fun viewDettached() {
        client.deleteObserver(this)
        client.clear()
    }


    fun onSendClicked(message: String) {
        val mes = Message(type = "text", text = message, time = System.currentTimeMillis(), id = 1)
        client.sendMessage(mes)
    }

    override fun update(o: Observable?, arg: Any?) {
        MyLog.show("update $this")
        if (arg is ArrayList<*>)
            view?.setMessage(arg as ArrayList<Message>)

    }

/*    fun sendImage(b: ByteArray?) {
        client.sendImage(b)
    }*/

    fun settingsClicked() {
        viewRouter.showInputServerScreen()
    }

    fun getPhotoClicked() {
        client.getCurrentPhoto()
        //view!!.showPhotosDialog()
    }

    fun exitClicked() {
        viewRouter.exitApp()
    }

    fun onTopicClicked(topic: String) {
        val text = App.instance.getString(R.string.delete_topic) + " \"$topic\"?"
        viewRouter.showConfirmDialog(text) {
            client.unsubscribeTopic(topic) { topics ->
                view?.showTopics(topics.toList())
            }
        }
    }

    fun onAddTopicClicked() {
        view?.showAddTopicDialog()
    }

    fun onSubscribeClicked(name: String) {
        client.subscribeToTopic(name) { topics ->
            view?.showTopics(topics.toList())
        }
    }
}