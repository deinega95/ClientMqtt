package com.deinega95.clientMqtt.view.fragments.interfaces

import com.deinega95.clientMqtt.model.Message

interface ITopicFragment {
    fun showError(mess: String?)
    fun setMessage(messages: List<Message>)
    fun showAddTopicDialog()
    fun showTopics(topics: List<String>)
    fun showSelectGetPhotoDialog()
    // fun showPhotosDialog()
}