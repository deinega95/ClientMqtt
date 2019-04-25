package com.deinega95.clientMqtt.presenter

import com.deinega95.clientMqtt.di.scopes.ImageViewerScope
import com.deinega95.clientMqtt.services.MqttService
import com.deinega95.clientMqtt.services.ViewRouter
import com.deinega95.clientMqtt.view.fragments.interfaces.IImageViewerFragment
import javax.inject.Inject

@ImageViewerScope
class ImageViewerPresenter @Inject constructor() : BasePresenter<IImageViewerFragment>() {

    @Inject
    lateinit var viewRouter: ViewRouter

    @Inject
    lateinit var mqttService: MqttService


    private var photoId: Long? = null

    override fun viewAttached() {
        val images = mqttService.photoByPeriod
        val startPosition = mqttService.photoByPeriod.indexOfFirst { it.id == photoId }
        view?.setContent(images, startPosition)
    }

    override fun viewDettached() {
        photoId = null
    }

    fun setParams(photoId: Long) {
        this.photoId = photoId
    }

    fun onCloseClicked() {
        viewRouter.closeFragment()
    }
}