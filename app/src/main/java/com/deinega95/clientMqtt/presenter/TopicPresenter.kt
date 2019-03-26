package com.deinega95.clientMqtt.presenter

import com.deinega95.clientMqtt.di.scopes.MainScope
import com.deinega95.clientMqtt.services.MqttService
import com.deinega95.clientMqtt.storage.PrefsManager
import com.deinega95.clientMqtt.view.fragments.interfaces.ITopicFragment
import javax.inject.Inject

@MainScope
class TopicPresenter  @Inject constructor() : BasePresenter<ITopicFragment>() {
    @Inject
    lateinit var client: MqttService

    @Inject
    lateinit var prefsManager: PrefsManager

    override fun viewDettached() {

    }

    override fun viewAttached() {

    }


    fun onSendClicked(message: String) {
        client.connect{isSuccess, mess->
            if (isSuccess){
                //     view?.showMainActivity()
                client.sendMessage(message, "Top2")
            }else{
                view?.showError(mess)
            }
        }
    }
}