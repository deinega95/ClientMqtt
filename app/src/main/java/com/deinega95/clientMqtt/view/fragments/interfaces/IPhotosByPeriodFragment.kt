package com.deinega95.clientMqtt.view.fragments.interfaces

import com.deinega95.clientMqtt.model.Message

interface IPhotosByPeriodFragment {
    fun setContent(photos: List<Message>, allPhotosCount: Int)
}