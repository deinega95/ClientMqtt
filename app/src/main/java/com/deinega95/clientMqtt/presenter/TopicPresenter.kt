package com.deinega95.clientMqtt.presenter

import android.util.Log
import com.deinega95.clientMqtt.R
import com.deinega95.clientMqtt.di.App
import com.deinega95.clientMqtt.di.scopes.MainScope
import com.deinega95.clientMqtt.model.Message
import com.deinega95.clientMqtt.services.MqttService
import com.deinega95.clientMqtt.services.MqttService.Companion.TOPIC
import com.deinega95.clientMqtt.services.ViewRouter
import com.deinega95.clientMqtt.storage.DbManager
import com.deinega95.clientMqtt.utils.MyLog
import com.deinega95.clientMqtt.view.fragments.interfaces.ITopicFragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import java.util.*
import javax.inject.Inject
import com.deinega95.clientMqtt.view.fragments.ImageViewerFragment.Companion.ImageViewerTypeEnum.Message as Mes

@MainScope
class TopicPresenter @Inject constructor() : BasePresenter<ITopicFragment>(), Observer {
    @Inject
    lateinit var client: MqttService
    @Inject
    lateinit var viewRouter: ViewRouter
    @Inject
    lateinit var dbManager: DbManager


    override fun viewAttached() {
        MyLog.show("!!prese viewAttached")
        view?.setMessage(dbManager.getMessages())
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
        MyLog.show("!!!!sendFirebaseToken in topicpres")
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                MyLog.show("!!!!task.isSuccessful=${task.isSuccessful}")
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }

                val token = task.result?.token
                MyLog.show("!!!token= $token")
                if (token != null) {
                    client.sendFirebaseToken(token)
                }
            })
    }


    override fun viewDettached() {
        MyLog.show("!!prese viewDettached")
        client.deleteObserver(this)
    }


    fun onSendClicked(message: String) {
        val mes = Message().apply {
            type = "text"
            text = message
            time = System.currentTimeMillis()
            id = System.currentTimeMillis()
        }
        client.sendMessage(mes)
    }

    override fun update(o: Observable?, arg: Any?) {
        MyLog.show("update $arg")
        if (arg is List<*>)
            view?.setMessage(arg as List<Message>)

    }

/*    fun sendImage(b: ByteArray?) {
        client.sendImage(b)
    }*/

    fun settingsClicked() {
        viewRouter.showInputServerScreen()
    }

    fun getPhotoClicked() {
        view?.showSelectGetPhotoDialog()
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

    fun onCurrentPhotoClicked() {
        client.getCurrentPhoto()
    }

    fun onPeriodPhotoClicked() {
        viewRouter.startPhotosActivity()
    }

    fun onClearMessagesClicked() {
        viewRouter.showConfirmDialog(R.string.confirm_clear_message) {
            dbManager.clearDb()
            client.clearMessages()
        }
    }

    fun onImageClicked(message: Message) {
        viewRouter.showImageViewer(Mes, message.id!!)
    }
}