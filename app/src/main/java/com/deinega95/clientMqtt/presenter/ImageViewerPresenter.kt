package com.deinega95.clientMqtt.presenter

import com.deinega95.clientMqtt.di.scopes.ImageViewerScope
import com.deinega95.clientMqtt.services.MqttService
import com.deinega95.clientMqtt.services.ViewRouter
import com.deinega95.clientMqtt.view.fragments.ImageViewerFragment
import com.deinega95.clientMqtt.view.fragments.interfaces.IImageViewerFragment
import javax.inject.Inject

@ImageViewerScope
class ImageViewerPresenter @Inject constructor() : BasePresenter<IImageViewerFragment>() {

    @Inject
    lateinit var viewRouter: ViewRouter

    @Inject
    lateinit var mqttService: MqttService


    private var photoId: Long? = null
    private var type: ImageViewerFragment.Companion.ImageViewerTypeEnum? = null

    override fun viewAttached() {
        val images = when (type) {
            ImageViewerFragment.Companion.ImageViewerTypeEnum.PhotoByPeriod -> mqttService.photoByPeriod
            ImageViewerFragment.Companion.ImageViewerTypeEnum.Message -> mqttService.messages.filter { it.type == "image" }
            null -> throw RuntimeException("invalid type in imageviewer")
        }

        val startPosition = images.indexOfFirst { it.id == photoId }
        view?.setContent(images, startPosition)

    }

    override fun viewDettached() {
        photoId = null
        type = null
    }

    fun setParams(type: ImageViewerFragment.Companion.ImageViewerTypeEnum, photoId: Long) {
        this.photoId = photoId
        this.type = type
    }

    fun onCloseClicked() {
        viewRouter.closeFragment()
    }
}