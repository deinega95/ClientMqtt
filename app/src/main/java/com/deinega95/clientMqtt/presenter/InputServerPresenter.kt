package com.deinega95.clientMqtt.presenter

import com.deinega95.clientMqtt.R
import com.deinega95.clientMqtt.di.scopes.AuthorizationScope
import com.deinega95.clientMqtt.services.MqttService
import com.deinega95.clientMqtt.services.ViewRouter
import com.deinega95.clientMqtt.storage.PrefsManager
import com.deinega95.clientMqtt.view.fragments.interfaces.IInputServerFragment
import javax.inject.Inject

@AuthorizationScope
class InputServerPresenter @Inject constructor() : BasePresenter<IInputServerFragment>() {
    @Inject
    lateinit var client: MqttService

    @Inject
    lateinit var prefsManager: PrefsManager
    @Inject
    lateinit var viewRouter: ViewRouter


    override fun viewAttached() {
        view?.setContent(prefsManager.getServer(), prefsManager.getUsername(), prefsManager.getPassword())
    }

    fun onConnectClicked(address: String, username: String, password: String) {
        if (address.isNotEmpty()) {
            prefsManager.saveServer(address, username, password)

            client.connect { isSuccess, mess ->
                if (isSuccess) {
                    viewRouter.showMainActivity()
                } else {
                    viewRouter.showError(mess)
                }
            }
        } else {
            viewRouter.showError(R.string.enter_server)
        }
    }

    override fun viewDettached() {

    }
}