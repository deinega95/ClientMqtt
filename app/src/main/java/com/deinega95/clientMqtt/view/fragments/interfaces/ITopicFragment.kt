package com.deinega95.clientMqtt.view.fragments.interfaces

import com.deinega95.clientMqtt.model.Message

interface ITopicFragment {
    fun showError(mess: String?)
    fun setMessage(messages: ArrayList<Message>)
    fun showAddTopicDialog()
    fun showTopics(topics: List<String>)
    // fun showPhotosDialog()
}