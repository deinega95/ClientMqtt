package com.deinega95.clientMqtt.presenter

import android.widget.Toast
import com.deinega95.clientMqtt.di.scopes.AuthorizationScope
import com.deinega95.clientMqtt.services.MqttService
import com.deinega95.clientMqtt.storage.PrefsManager
import com.deinega95.clientMqtt.view.fragments.interfaces.IInputServerFragment
import org.eclipse.paho.client.mqttv3.MqttClient
import javax.inject.Inject

@AuthorizationScope
class InputServerPresenter @Inject constructor() : BasePresenter<IInputServerFragment>() {
    @Inject
    lateinit var client: MqttService

    @Inject
    lateinit var prefsManager: PrefsManager

    override fun viewDettached() {

    }

    override fun viewAttached() {

    }

    fun onConnectClicked(address: String, clientId: String) {
        prefsManager.saveServer(address, clientId)

        client.connect{isSuccess, mess->
            if (isSuccess){
                view?.showMainActivity()
            }else{
                view?.showError(mess)
            }
        }
    }
}