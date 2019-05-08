package com.deinega95.clientMqtt.presenter

import com.deinega95.clientMqtt.di.scopes.PhotoByPeriodScope
import com.deinega95.clientMqtt.model.Message
import com.deinega95.clientMqtt.services.MqttService
import com.deinega95.clientMqtt.services.MqttService.Companion.PHOTO_BY_PERIOD_UPDATED
import com.deinega95.clientMqtt.services.ViewRouter
import com.deinega95.clientMqtt.view.fragments.interfaces.IPhotosByPeriodFragment
import java.util.*
import javax.inject.Inject

@PhotoByPeriodScope
class PhotosByPeriodPresenter @Inject constructor() : BasePresenter<IPhotosByPeriodFragment>(), Observer {
    @Inject
    lateinit var viewRouter: ViewRouter

    @Inject
    lateinit var mqttService: MqttService

    override fun viewAttached() {
        mqttService.addObserver(this)
        setContent()
    }


    override fun viewDettached() {
        mqttService.deleteObserver(this)
    }

    override fun update(obs: Observable?, obj: Any?) {
        if (obj is String && obj == PHOTO_BY_PERIOD_UPDATED) setContent()
    }

    private fun setContent() {
        view?.setContent(mqttService.photoByPeriod, mqttService.countAllPhotoByPeriod)
    }

    fun onItemClicked(message: Message) {
        viewRouter.showImageViewer(message.id!!)
    }
}