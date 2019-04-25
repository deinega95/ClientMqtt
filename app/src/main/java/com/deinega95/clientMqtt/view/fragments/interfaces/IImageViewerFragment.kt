package com.deinega95.clientMqtt.view.fragments.interfaces

import com.deinega95.clientMqtt.model.Message

interface IImageViewerFragment {
    fun setContent(photos: List<Message>, pos: Int)
}