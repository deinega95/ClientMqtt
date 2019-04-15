package com.deinega95.clientMqtt.presenter

import com.deinega95.clientMqtt.di.scopes.MainScope
import com.deinega95.clientMqtt.model.Message
import com.deinega95.clientMqtt.services.MqttService
import com.deinega95.clientMqtt.services.MqttService.Companion.TOPIC
import com.deinega95.clientMqtt.services.ViewRouter
import com.deinega95.clientMqtt.storage.PrefsManager
import com.deinega95.clientMqtt.utils.MyLog
import com.deinega95.clientMqtt.view.fragments.interfaces.ITopicFragment
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
        client.connect{isConnect, error ->
            if (isConnect){
                client.addObserver(this)
                client.subscribeToTopic(TOPIC){
                    view?.showTopics(it)
                }
            }else {
                viewRouter.showError(error)
            }

        }
    }


    override fun viewDettached() {
        client.deleteObserver(this)
    }


    fun onSendClicked(message: String) {
        client.sendMessage(message)
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
        //view!!.showPhotosDialog()
    }

    fun exitClicked() {
        viewRouter.exitApp()
    }

    fun onTopicClicked(name: String) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun onAddTopicClicked() {
        view?.showAddTopicDialog()
    }

    fun onSubscribeClicked(name: String) {
        client.subscribeToTopic(name){
            view?.showTopics(it)
        }
    }
}